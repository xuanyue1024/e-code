package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ecode.enumeration.UserRole;
import com.ecode.enumeration.UserSex;
import com.ecode.enumeration.UserStatus;
import com.ecode.json.S3UrlSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * User对象
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-09-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@Schema(description="User数据库对象")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "身份id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "角色")
    private UserRole role;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "账号状态 0：禁用，1：启用")
    private UserStatus status;

    @Schema(description = "昵称")
    private String name;

    @Schema(description = "头像链接")
    @JsonSerialize(using = S3UrlSerializer.class)
    private String profilePicture;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别 0：男，1：女")
    private UserSex sex;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "积分")
    private Long score;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "创造时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


}
