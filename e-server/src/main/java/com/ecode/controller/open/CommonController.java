package com.ecode.controller.open;

import com.ecode.constant.MessageConstant;
import com.ecode.result.Result;
import com.ecode.service.CaptchaService;
import com.ecode.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/open")
@Tag(name = "开放接口")
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final FileService fileService;

    private final CaptchaService captchaService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "data值为上传返回的链接")
    public Result<String> upload(@RequestPart("file") MultipartFile file){
        log.info("文件上传：{}",file);

        String url = fileService.uploadFile(file);

        if (url != null){
            return Result.success(url);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    @GetMapping("/captcha/getCaptcha")
    @Operation(summary = "发送验证码")
    public Result sendCaptcha(String email){
        captchaService.sendCaptcha(email);
        return Result.success();
    }
}