package com.ecode.controller.open;

import com.ecode.result.Result;
import com.ecode.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码控制器
 *
 * @author 竹林听雨
 * @date 2024/09/25
 */
@Slf4j
@RequestMapping("/open/captcha")
@RestController
@Api(tags = "验证码")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/getCaptcha")
    @ApiOperation("发送验证码")
    public Result sendCaptcha(String email){
        captchaService.sendCaptcha(email);
        return Result.success();
    }

}
