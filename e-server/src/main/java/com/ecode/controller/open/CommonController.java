package com.ecode.controller.open;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.ecode.annotation.Captcha;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    private final ImageCaptchaApplication imageCaptchaApplication;

    private static final List<String> CAPTCHA_TYPES = Arrays.asList(
            CaptchaTypeConstant.SLIDER,
            CaptchaTypeConstant.ROTATE,
            CaptchaTypeConstant.CONCAT,
            CaptchaTypeConstant.WORD_IMAGE_CLICK
    );

    private static final Random RANDOM = new Random();

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
    @Operation(summary = "发送邮件验证码")
    @Captcha
    public Result sendCaptcha(String email){
        captchaService.sendCaptcha(email);
        return Result.success();
    }

    @PostMapping("/captcha/check")
    public ApiResponse<?> checkCaptcha(@RequestBody Data data) {
        ApiResponse<?> response = imageCaptchaApplication.matching(data.getId(), data.getData());
        if (response.isSuccess()) {
            // 验证码验证成功，此处应该进行自定义业务处理， 或者返回验证token进行二次验证等。
            return ApiResponse.ofSuccess(Collections.singletonMap("validToken", data.getId()));
        }
        return response;
    }

    @RequestMapping("/captcha/gen")
    public ApiResponse<ImageCaptchaVO> genCaptcha() {
        // 1.生成验证码(该数据返回给前端用于展示验证码数据)
        // 参数1为具体的验证码类型， 默认支持 SLIDER、ROTATE、WORD_IMAGE_CLICK、CONCAT 等验证码类型，详见： `CaptchaTypeConstant`类
        return imageCaptchaApplication.generateCaptcha(getRandomCaptchaType());
    }

    @lombok.Data
    public static class Data {
        private String  id;
        private ImageCaptchaTrack data;
    }

    public static String getRandomCaptchaType() {
        return CAPTCHA_TYPES.get(RANDOM.nextInt(CAPTCHA_TYPES.size()));
    }
}