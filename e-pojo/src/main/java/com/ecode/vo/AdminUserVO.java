package com.ecode.vo;

import com.ecode.enumeration.UserRole;
import com.ecode.enumeration.UserSex;
import com.ecode.enumeration.UserStatus;
import com.ecode.json.S3UrlSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 管理员用户信息响应对象。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Schema(description = "管理员用户信息响应对象")
public class AdminUserVO implements Serializable {

    private static final long serialVersionUID = 7294792400922245230L;

    @Schema(description = "用户id")
    private Integer id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户角色")
    private UserRole role;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "账号状态")
    private UserStatus status;

    @Schema(description = "昵称")
    private String name;

    @JsonSerialize(using = S3UrlSerializer.class)
    @Schema(description = "头像链接")
    private String profilePicture;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别")
    private UserSex sex;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "积分")
    private Long score;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
