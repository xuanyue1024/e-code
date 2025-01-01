package com.ecode.dto;

import com.ecode.enumeration.UserRole;
import com.ecode.enumeration.UserSex;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
@Data
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = -3176777875390385317L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色")
    private UserRole role;

    @ApiModelProperty(value = "邮箱")
    private String email;


    @ApiModelProperty(value = "昵称")
    private String name;

    @ApiModelProperty(value = "头像链接")
    private String profilePicture;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "性别 0：男，1：女")
    private UserSex sex;

    @ApiModelProperty(value = "地址")
    private String address;

    /*@ApiModelProperty(value = "积分")
    private Long score;*/

    @ApiModelProperty(value = "出生日期")
    private LocalDate birthDate;
}
