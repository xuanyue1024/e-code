package com.ecode.controller.user;


import cn.dev33.satoken.stp.StpUtil;
import com.ecode.constant.JwtClaimsConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.DanmakuDTO;
import com.ecode.dto.SRSCallbackDTO;
import com.ecode.enumeration.Redis;
import com.ecode.enumeration.UserRole;
import com.ecode.json.DanmakuMessage;
import com.ecode.result.Result;
import com.ecode.service.LiveService;
import com.ecode.websocket.LiveWebSocket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 直播
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-01  14:50
 */

@Tag(name = "直播管理")
@RequestMapping("/user/live")
@RestController("userLiveController")
@RequiredArgsConstructor
@Slf4j
public class LiveController {

    private final LiveService liveService;

    private final RedisTemplate redisTemplate;


    @PostMapping("/danmaku/send")
    @Operation(summary = "发送弹幕")
    public Result sendDanmaku(@Valid @RequestBody DanmakuDTO danmakuDTO) {
        DanmakuMessage danmakuMessage = DanmakuMessage.builder()
                .userId(BaseContext.getCurrentId())
                .username((String) StpUtil.getExtra(JwtClaimsConstant.USERNAME))
                .msg(danmakuDTO.getMsg())
                .color(Optional.ofNullable(danmakuDTO.getColor()).orElse("#000000"))
                .size(danmakuDTO.getSize())
                .ts(System.currentTimeMillis())
                .build();

        if (BaseContext.getCurrentRole() == UserRole.TEACHER) {
            danmakuMessage.setRole(0);
        }

        liveService.sendDanmaku(danmakuDTO.getClassId(), danmakuMessage);

        LiveWebSocket.broadcastToRoom(danmakuDTO.getClassId(), danmakuMessage);

        return Result.success();
    }

    @GetMapping("/danmaku/list/{classId}")
    @Operation(summary = "获取100条历史弹幕")
    public Result<List<DanmakuMessage>> getDanmakuList(@PathVariable Integer classId) {
        List<DanmakuMessage> latestDanmakuList = liveService.getLatestDanmaku(classId, 100);

        return Result.success(latestDanmakuList);
    }

    @PostMapping("/callback")
    @Operation(summary = "SRS媒体服务器回调函数")
    public Result streamsCallback(@RequestBody SRSCallbackDTO srsCallbackDTO){

        if (srsCallbackDTO.getAction() == null || srsCallbackDTO.getStream() == null) {
            log.warn("SRS回调: 缺少必要参数 action 或 stream");
            return Result.error("缺少必要参数");
        }

        String key  = Redis.LIVE_STATUS + srsCallbackDTO.getStream();
        int status = 0;//0-关闭,1-开启
        switch (srsCallbackDTO.getAction()){
            case "on_publish":
                log.info("SRS回调: 用户 {} 开始推流到 {}", srsCallbackDTO.getClientId(), srsCallbackDTO.getStream());
                status = 1;
                break;
            case "on_unpublish":
                log.info("SRS回调: 用户 {} 停止推流 {}", srsCallbackDTO.getClientId(), srsCallbackDTO.getStream());
                redisTemplate.delete(key);
                break;
            default:
                log.warn("SRS回调: 未知的 action {}", srsCallbackDTO.getAction());
        }
        LiveWebSocket.broadcastToRoom(Integer.valueOf(srsCallbackDTO.getStream()), Map.of("status", status));
        redisTemplate.opsForValue().set(key, status);

        return Result.success(0, null, null);
    }
}
