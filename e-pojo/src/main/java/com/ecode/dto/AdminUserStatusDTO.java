package com.ecode.dto;

import com.ecode.enumeration.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员修改用户状态请求参数。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Schema(description = "管理员修改用户状态请求参数")
public class AdminUserStatusDTO implements Serializable {

    private static final long serialVersionUID = -7971571855372582792L;

    @NotNull(message = "账号状态不能为空")
    @Schema(description = "账号状态，可选值：ENABLE、DISABLE", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserStatus status;
}
