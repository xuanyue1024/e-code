package com.ecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "用户密码登录时传递的数据模型")
public class UserLoginByPasswdDTO implements Serializable {

    private static final long serialVersionUID = 1746921078500349443L;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}
