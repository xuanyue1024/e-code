package com.ecode.dto;

import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录dto
 *
 * @author 竹林听雨
 * @date 2024/09/21
 */
@Data
@Schema(description = "用户登录时传递的统一数据模型")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = -4381023824880508122L;

    @Schema(description = "登录方式")
    private String loginType;

    //passwd
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;

    //email
    @Schema(description = "邮箱号")
    private String email;
    @Schema(description = "邮箱验证码")
    private String emailCode;

    //passkey
    @Schema(description = "passkey验证数据")
    private String identifier;
    private PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential;

    //OAuth2
    @Schema(description = "oauth2 code")
    private String authCode; // 第三方返回的 code
    @Schema(description = "oauth2 state")
    private String state;
}
