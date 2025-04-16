package com.ecode.dto;

import com.ecode.enumeration.UserRole;
import com.ecode.enumeration.UserSex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
@Data
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = -3176777875390385317L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "角色")
    private UserRole role;

    @Schema(description = "邮箱")
    private String email;


    @Schema(description = "昵称")
    private String name;

    @Schema(description = "头像链接")
    private String profilePicture;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别 0：男，1：女")
    private UserSex sex;

    @Schema(description = "地址")
    private String address;

    /*@Schema(description = "积分")
    private Long score;*/

    @Schema(description = "出生日期")
    private LocalDate birthDate;
}
