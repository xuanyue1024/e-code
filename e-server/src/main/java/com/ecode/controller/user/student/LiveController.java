package com.ecode.controller.user.student;


import com.ecode.annotation.DataAccessCheck;
import com.ecode.enumeration.OperationType;
import com.ecode.enumeration.Redis;
import com.ecode.enumeration.ResponseCode;
import com.ecode.result.Result;
import com.ecode.utils.WebRTCUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 直播
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-05  00:45
 */

@Tag(name = "直播管理")
@RestController("studentLiveController")
@RequestMapping("/student/live")
@RequiredArgsConstructor
public class LiveController {

    private final WebRTCUtil webRTCUtil;

    private final RedisTemplate redisTemplate;


    /**
     * 转发 WHEP 拉流请求
     */
    @PostMapping("/whep/{classId}")
    @Operation(summary = "直播拉流")
    @DataAccessCheck(value = OperationType.STUDENT_TO_CLASS, paramIndex = 0)
    public Object proxyWhep(@PathVariable Integer classId, @RequestBody String sdpOffer) {
        Integer status = (Integer) redisTemplate.opsForValue().get(Redis.LIVE_STATUS.toString() + classId);
        if (status == null || status != 1) {
            return Result.success(ResponseCode.LIVE_NO_START.getValue(), null, ResponseCode.LIVE_NO_START.getDesc());
        }

        String sdpAnswer = webRTCUtil.start(WebRTCUtil.Status.WHEP, String.valueOf(classId), sdpOffer);

        return ResponseEntity
                .status(HttpStatus.CREATED) // WHIP 要求 201
                .contentType(MediaType.valueOf("application/sdp"))
                .body(sdpAnswer);
    }
}
