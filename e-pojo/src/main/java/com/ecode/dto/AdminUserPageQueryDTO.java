package com.ecode.dto;

import com.ecode.enumeration.UserRole;
import com.ecode.enumeration.UserStatus;
import com.ecode.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 管理员用户分页查询参数。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "管理员用户分页查询参数")
public class AdminUserPageQueryDTO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 5055619439978364486L;

    @Schema(description = "名称、用户名或邮箱关键字")
    private String keyword;

    @Schema(description = "用户角色，可选值：ADMIN、TEACHER、STUDENT")
    private UserRole role;

    @Schema(description = "账号状态，可选值：ENABLE、DISABLE")
    private UserStatus status;
}
