package com.ecode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证码校验注解
 * <p>
 * 把注解加载Controller中,进行统一验证码校验
 *
 * @author 竹林听雨
 * @date 2026/01/09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Captcha {
}
