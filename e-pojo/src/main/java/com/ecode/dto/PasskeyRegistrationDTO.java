package com.ecode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Passkey注册
 *
 * 该类用于表示Passkey注册信息。
 * 包含凭证名称和凭证体。
 *
 * @author 竹林听雨
 * @date 2025/04/04
 */
@Data
public class PasskeyRegistrationDTO implements Serializable {
    private static final long serialVersionUID = -4828296437342536611L;

    @Schema(description = "凭证名称")
    private String name;
    @Schema(description = "凭证体")
    private String credential;

}
