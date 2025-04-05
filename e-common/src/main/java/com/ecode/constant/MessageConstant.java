package com.ecode.constant;

/**
 * 信息提示常量类
 */
public class MessageConstant {

    public static final String PASSWORD_ERROR = "密码错误";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String ACCOUNT_LOCKED = "账号被锁定,请联系相关管理员";
    public static final String SQL_UNKNOWN_ERROR = "数据库未知错误";
    public static final String USER_NOT_LOGIN = "用户未登录";
    public static final String CATEGORY_BE_RELATED_BY_SETMEAL = "当前分类关联了套餐,不能删除";
    public static final String CATEGORY_BE_RELATED_BY_DISH = "当前分类关联了菜品,不能删除";
    public static final String SHOPPING_CART_IS_NULL = "购物车数据为空，不能下单";
    public static final String ADDRESS_BOOK_IS_NULL = "用户地址为空，不能下单";
    public static final String LOGIN_FAILED = "登录失败";
    public static final String LOGIN_FAILED_USERNAME_OR_PASSWD = "登录失败,请检查用户名或密码";
    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String SETMEAL_ENABLE_FAILED = "套餐内包含未启售菜品，无法启售";
    public static final String PASSWORD_EDIT_FAILED = "密码修改失败";
    public static final String DISH_ON_SALE = "起售中的菜品不能删除";
    public static final String SETMEAL_ON_SALE = "起售中的套餐不能删除";
    public static final String DISH_BE_RELATED_BY_SETMEAL = "当前菜品关联了套餐,不能删除";
    public static final String ORDER_STATUS_ERROR = "订单状态错误";
    public static final String ORDER_NOT_FOUND = "订单不存在";
    public static final String ALREADY_EXISTS = "已存在";
    public static final String JWT_FAILS = "The login failed and the token became invalid";
    public static final String PASSKEY_VERIFY_FAILED = "通行秘钥验证失败";

    public static final String INVALID_LOGIN_TYPE = "无效的登录类型";
    public static final String UPDATE_FAILED = "修改失败";

    //验证码
    public static final String CAPTCHA_TRY_AGAIN_WITHIN24_HOURS = "验证码发送过于频繁,请24小时后再试";
    public static final String CAPTCHA_TRY_AGAIN_WITHIN_Null_S = "验证码发送过于频繁,请在%s秒后重试";
    public static final String CAPTCHA_ERROR = "验证码发送出现错误,请联系管理员";

    //注册
    public static final String REGISTRATION_FAILED_CAPTCHA = "注册失败,验证码不正确";

    public static final String REGISTRATION_FAILED = "注册失败";

    //代码调试
    public static final String NO_LANGUAGE = "没有此类编程语言";

    //权限问题
    public static final String ACCESS_DENIED = "你没有权限访问此接口";
    public static final String  TOKEN_FAILURE  =  "token失效";

    //班级
    public static final String INVITATIONCODE_NOT_FOUND = "班级邀请码不存在";
    public static final String EXIT_FAILURE_NOT_EXIST_CLASS = "退出班级失败，包含不存在的班级";
    public static final String CLASS_AND_TEACHER_NOT_FOUND = "当前老师班级内没有该班级";
    public static final String CLASS_AND_STUDENT_NOT_FOUND = "当前学生不在该班级";

    public static final String INVALID_FORMAT_FAILURE = "输入格式有误";
    public static final String DATA_NOT_FOUND = "数据不存在";

    //题目
    public static final String PROBLEM_NOT_FOUND = "题目不存在";

}
