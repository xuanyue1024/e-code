package com.ecode.aspect;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.plugins.secondary.SecondaryVerificationApplication;
import com.ecode.constant.MessageConstant;
import com.ecode.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static java.util.Objects.requireNonNull;

/**
 * 验证码校验AOP
 * <p>
 * 把注解加载Controller中,进行统一验证码校验
 *
 * @author 竹林听雨
 * @date 2026/01/09
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CaptchaAspect {

    private final ImageCaptchaApplication imageCaptchaApplication;

    @Around("@annotation(com.ecode.annotation.Captcha)")
    public Object captchaBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) requireNonNull(RequestContextHolder
                .getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();

        String captchaToken = request.getHeader("Captcha-Token");

        if (captchaToken == null || captchaToken.isEmpty()) {
            throw new BaseException("接口需要验证码校验", 6000);
        }else {
            if (imageCaptchaApplication instanceof SecondaryVerificationApplication) {
                // 参数1为具体的验证码类型， 默认支持 SLIDER、ROTATE、WORD_IMAGE_CLICK、CONCAT 等验证码类型，详见： `CaptchaTypeConstant`类
                boolean valid = ((SecondaryVerificationApplication) imageCaptchaApplication).secondaryVerification(captchaToken);
                if (valid) {
                    return joinPoint.proceed();
                }
            }
            log.error("验证码校验失败，captchaToken={}", captchaToken);
            throw new BaseException(MessageConstant.CAPTCHA_VERIFICATION_FAILED);
        }

    }
}
