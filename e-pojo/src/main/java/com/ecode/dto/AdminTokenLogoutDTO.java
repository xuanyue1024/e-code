package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员指定 token 下线请求参数。
 *
 * @author 竹林听雨
 * @Assisted-by GPT-5
 */
@Data
@Schema(description = "管理员指定 token 下线请求参数")
public class AdminTokenLogoutDTO implements Serializable {

    private static final long serialVersionUID = 2568176336828548351L;

    @NotBlank(message = "token不能为空")
    @Schema(description = "需要失效的 JWT token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;
}
