package com.ecode.dto;

import com.ecode.enumeration.UserRole;
import com.ecode.enumeration.UserSex;
import com.ecode.enumeration.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 管理员更新用户请求参数。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Schema(description = "管理员更新用户请求参数")
public class AdminUserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 142153964689860328L;

    @NotNull(message = "用户id不能为空")
    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码，为空时不修改密码")
    private String password;

    @Schema(description = "用户角色，可选值：ADMIN、TEACHER、STUDENT")
    private UserRole role;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "账号状态，可选值：ENABLE、DISABLE")
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

    @Schema(description = "积分")
    private Long score;

    @Schema(description = "出生日期")
    private LocalDate birthDate;
}
