package com.ecode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 竹林听雨
 * @since 2026-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("oauth_identities")
@Schema(description = "OauthIdentities对象")
public class OauthIdentities implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "关联user表ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "第三方平台名称")
    @TableField("provider")
    private String provider;

    @Schema(description = "第三方平台ID")
    @TableField("provider_id")
    private String providerId;

    @Schema(description = "第三方平台用户名")
    @TableField("provider_username")
    private String providerUsername;

    @Schema(description = "第三方平台邮箱")
    @TableField("provider_email")
    private String providerEmail;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
