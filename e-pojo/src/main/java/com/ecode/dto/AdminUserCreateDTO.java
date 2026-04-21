package com.ecode.dto;

import com.ecode.enumeration.UserRole;
import com.ecode.enumeration.UserSex;
import com.ecode.enumeration.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 管理员创建用户请求参数。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Schema(description = "管理员创建用户请求参数")
public class AdminUserCreateDTO implements Serializable {

    private static final long serialVersionUID = 185326326072597305L;

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotNull(message = "角色不能为空")
    @Schema(description = "用户角色，可选值：ADMIN、TEACHER、STUDENT", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserRole role;

    @NotBlank(message = "邮箱不能为空")
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "账号状态，可选值：ENABLE、DISABLE，默认 ENABLE")
    private UserStatus status;

    @Schema(description = "昵称")
    private String name;

    @Schema(description = "头像对象名")
    private String profilePicture;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别，可选值：MALE、FEMALE")
    private UserSex sex;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "积分，默认 0")
    private Long score;

    @Schema(description = "出生日期")
    private LocalDate birthDate;
}
