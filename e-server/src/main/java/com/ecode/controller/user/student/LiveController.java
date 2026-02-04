package com.ecode.controller.user.student;


import cn.dev33.satoken.stp.StpUtil;
import com.ecode.annotation.DataAccessCheck;
import com.ecode.constant.JwtClaimsConstant;
import com.ecode.context.BaseContext;
import com.ecode.dto.DanmakuDTO;
import com.ecode.enumeration.OperationType;
import com.ecode.json.DanmakuMessage;
import com.ecode.result.Result;
import com.ecode.service.LiveService;
import com.ecode.utils.WebRTCUtil;
import com.ecode.websocket.DanmakuWebSocket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 直播
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-05  00:45
 */

@Tag(name = "直播管理")
@RestController
@RequestMapping("/student/live")
@Component("studentLiveController")
@RequiredArgsConstructor
public class LiveController {

    private final WebRTCUtil webRTCUtil;

    private final LiveService liveService;

    /**
     * 转发 WHIP 推流请求
     * @param classId 班级ID
     * @param sdpOffer 客户端生成的 SDP
     */
    /*@PostMapping("/whip/{classId}")
    @Operation(description = "直播推流")
    @DataAccessCheck(value = OperationType.TEACHER_TO_CLASS, paramIndex = 0)
    public Object proxyWhip(@PathVariable String classId,
                            @RequestBody String sdpOffer) {

        String sdpAnswer = webRTCUtil.start(WebRTCUtil.Status.WHIP, classId, sdpOffer);

        return ResponseEntity
                .status(HttpStatus.CREATED) // WHIP 要求 201
                .contentType(MediaType.valueOf("application/sdp")) // 关键！
                .body(sdpAnswer);
    }*/

    /**
     * 转发 WHEP 拉流请求
     */
    @PostMapping("/whep/{classId}")
    @Operation(summary = "直播拉流")
    @DataAccessCheck(value = OperationType.STUDENT_TO_CLASS, paramIndex = 0)
    public ResponseEntity<String> proxyWhep(@PathVariable String classId, @RequestBody String sdpOffer) {

        String sdpAnswer = webRTCUtil.start(WebRTCUtil.Status.WHEP, classId, sdpOffer);

        return ResponseEntity
                .status(HttpStatus.CREATED) // WHIP 要求 201
                .contentType(MediaType.valueOf("application/sdp"))
                .body(sdpAnswer);
    }

    @PostMapping("/danmaku/send")
    @Operation(summary = "发送弹幕")
    public Result sendDanmaku(@RequestBody DanmakuDTO danmakuDTO) {
        DanmakuMessage danmakuMessage = DanmakuMessage.builder()
                .userId(BaseContext.getCurrentId())
                .username((String) StpUtil.getExtra(JwtClaimsConstant.USERNAME))
                .msg(danmakuDTO.getMsg())
                .color(Optional.ofNullable(danmakuDTO.getColor()).orElse("#ffffff"))
                .ts(System.currentTimeMillis())
                .build();

        liveService.sendDanmaku(danmakuDTO.getClassId(), danmakuMessage);

        DanmakuWebSocket.broadcastToRoom(danmakuDTO.getClassId(), danmakuMessage);

        return Result.success();
    }

    @GetMapping("/danmaku/list/{classId}")
    @Operation(summary = "获取100条历史弹幕")
    public Result getDanmakuList(@PathVariable Integer classId) {
        List<DanmakuMessage> latestDanmakuList = liveService.getLatestDanmaku(classId, 100);

        return Result.success(latestDanmakuList);
    }
}
