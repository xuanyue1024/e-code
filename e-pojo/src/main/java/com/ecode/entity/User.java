package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author 竹林听雨
 * @since 2024-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "身份id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色")
    private String role;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "账号状态 0：禁用，1：启用")
    private String status;

    @ApiModelProperty(value = "昵称")
    private String name;

    @ApiModelProperty(value = "头像链接")
    private String profilePicture;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "性别 0：男，1：女")
    private String sex;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "出生日期")
    private LocalDate birthDate;

    @ApiModelProperty(value = "创造时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
