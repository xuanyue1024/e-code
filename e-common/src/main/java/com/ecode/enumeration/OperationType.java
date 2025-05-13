package com.ecode.enumeration;

/**
 * 操作类型枚举：定义不同的访问类型
 *与{@link com.ecode.annotation.DataAccessCheck}绑定
 * @author 竹林听雨
 * @date 2025/05/11
 */
public enum OperationType {

        /**
         * 老师对于班级的访问
         */
        TEACHER_TO_CLASS,

        /**
         * 学生对于班级的访问
         */
        STUDENT_TO_CLASS

}