package com.ecode.dto;

import com.ecode.enumeration.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户注册时传递的统一数据模型")
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = 7126128361502010286L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "注册用户角色")
    private UserRole role;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "邮箱验证码")
    private String emailCode;
}
