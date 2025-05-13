package com.ecode.annotation;

import com.ecode.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据访问检查注解，用于标记方法需要进行访问控制检查
 *比如老师是否有权限访问班级
 *
 * @author 竹林听雨
 * @date 2025/05/11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAccessCheck {

    //操作类型
    OperationType value();
    //参数位置序号,默认为0
    int paramIndex() default 0;

}
