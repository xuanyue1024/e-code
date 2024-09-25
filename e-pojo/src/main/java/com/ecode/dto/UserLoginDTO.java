package com.ecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录dto
 *
 * @author 竹林听雨
 * @date 2024/09/21
 */
@Data
@ApiModel(description = "用户登录时传递的统一数据模型")
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = -4381023824880508122L;

    @ApiModelProperty("登录方式")
    private String loginType;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邮箱号")
    private String email;

    @ApiModelProperty("邮箱验证码")
    private String emailCode;
}
