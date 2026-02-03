package com.ecode.controller.user.teacher;


import com.ecode.utils.WebRTCUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * 直播
 *
 * @author 竹林听雨
 * @version 1.0
 * @since 2026-02-01  14:50
 */

@Tag(name = "直播管理")
@RestController
@RequestMapping("/teacher/live")
@Component("teacherLiveController")
@RequiredArgsConstructor
public class LiveController {

    private final WebRTCUtil webRTCUtil;

    /**
     * 转发 WHIP 推流请求
     * @param streamKey 业务定义的流 ID
     * @param sdpOffer 客户端生成的 SDP
     */
    @PostMapping("/whip")
    public Object proxyWhip(HttpServletRequest request,
                            @RequestParam String streamKey,
                            @RequestBody String sdpOffer) {

        String sdpAnswer = webRTCUtil.start(WebRTCUtil.Status.WHIP, streamKey, sdpOffer);

        return ResponseEntity
                .status(HttpStatus.CREATED) // WHIP 要求 201
                .contentType(MediaType.valueOf("application/sdp")) // 关键！
                .body(sdpAnswer); // 纯字符串，不加引号
    }

    /**
     * 转发 WHEP 拉流请求
     */
    @PostMapping("/whep")
    public ResponseEntity<String> proxyWhep(@RequestParam String streamKey, @RequestBody String sdpOffer) {

        String sdpAnswer = webRTCUtil.start(WebRTCUtil.Status.WHEP, streamKey, sdpOffer);

        return ResponseEntity
                .status(HttpStatus.CREATED) // WHIP 要求 201
                .contentType(MediaType.valueOf("application/sdp")) // 关键！
                .body(sdpAnswer); // 纯字符串，不加引号
    }
}
