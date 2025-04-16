package com.ecode.dto;

import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录dto
 *
 * @author 竹林听雨
 * @date 2024/09/21
 */
@Data
@Schema(description = "用户登录时传递的统一数据模型")
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = -4381023824880508122L;

    @Schema(description = "登录方式")
    private String loginType;

    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;

    @Schema(description = "邮箱号")
    private String email;

    @Schema(description = "邮箱验证码")
    private String emailCode;

    @Schema(description = "passkey验证数据")
    private String identifier;
    private PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential;
}
