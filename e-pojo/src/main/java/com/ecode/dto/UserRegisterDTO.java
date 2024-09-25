package com.ecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "用户注册时传递的统一数据模型")
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = 7126128361502010286L;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("邮箱验证码")
    private String verificationCode;
}
