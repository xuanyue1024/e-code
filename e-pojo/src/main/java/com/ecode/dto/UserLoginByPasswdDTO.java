package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户密码登录时传递的数据模型")
public class UserLoginByPasswdDTO implements Serializable {

    private static final long serialVersionUID = 1746921078500349443L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;
}
