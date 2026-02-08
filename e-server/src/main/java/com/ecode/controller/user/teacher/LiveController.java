package com.ecode.controller.user.teacher;


import com.ecode.annotation.DataAccessCheck;
import com.ecode.enumeration.OperationType;
import com.ecode.utils.WebRTCUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 直播
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-01  14:50
 */

@Tag(name = "直播管理")
@RequestMapping("/teacher/live")
@RestController("teacherLiveController")
@RequiredArgsConstructor
public class LiveController {

    private final WebRTCUtil webRTCUtil;


    /**
     * 转发 WHIP 推流请求
     * @param classId 班级ID
     * @param sdpOffer 客户端生成的 SDP
     */
    @PostMapping("/whip/{classId}")
    @Operation(summary = "直播推流")
    @DataAccessCheck(value = OperationType.TEACHER_TO_CLASS, paramIndex = 0)
    public Object proxyWhip(@PathVariable Integer classId,
                            @RequestBody String sdpOffer) {
        String sdpAnswer = webRTCUtil.start(WebRTCUtil.Status.WHIP, String.valueOf(classId), sdpOffer);

        return ResponseEntity
                .status(HttpStatus.CREATED) // WHIP 要求 201
                .contentType(MediaType.valueOf("application/sdp")) // 关键！
                .body(sdpAnswer);
    }

}
