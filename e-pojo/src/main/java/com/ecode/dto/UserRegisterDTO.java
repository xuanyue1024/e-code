package com.ecode.dto;

import com.ecode.enumeration.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户注册时传递的统一数据模型")
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = 7126128361502010286L;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "注册用户角色")
    @NotNull(message = "角色不能为空")
    private UserRole role;

    @Schema(description = "邮箱")
    @NotNull(message = "邮箱不能为空")
    private String email;

    @Schema(description = "邮箱验证码")
    private String emailCode;

    @Schema(description = "注册code")
    private String registerCode;

    @AssertTrue(message = "邮箱验证码和注册code不能同时存在,也不能同时为空")
    public boolean isValidate() {
        if (emailCode == null || emailCode.isEmpty()) {
            return registerCode != null;
        }
        return true;
    }
}
