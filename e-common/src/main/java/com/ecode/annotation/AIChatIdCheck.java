package com.ecode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI聊天ID校验
 * 判断聊天ID是否符合格式以及是否所属登录用户
 *
 * @author 竹林听雨
 * @date 2026/01/07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIChatIdCheck {

}
