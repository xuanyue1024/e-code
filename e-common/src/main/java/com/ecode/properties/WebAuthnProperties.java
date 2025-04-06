package com.ecode.properties;

import com.yubico.webauthn.data.AttestationConveyancePreference;
import com.yubico.webauthn.data.AuthenticatorAttachment;
import com.yubico.webauthn.data.ResidentKeyRequirement;
import com.yubico.webauthn.data.UserVerificationRequirement;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "ecode.webauthn")
@Data
public class WebAuthnProperties {
    /**
     * 允许的来源，表示允许的来源 URL 集合
     */
    private Set<String> allowedOrigins;

    /**
     * {@code attestation }字段用来指示认证器（authenticator）生成的证书的类型。
     *
     * <ul>
     *   <li><b>direct</b>：
     *     <ul>
     *       <li>直接模式，需要认证器生成并返回完整的认证声明（attestation statement）。认证者可以通过这些声明验证认证器的真实性和完整性。</li>
     *     </ul>
     *   </li>
     *   <li><b>enterprise</b>：
     *     <ul>
     *       <li>企业模式，允许企业使用自己的认证器。认证器会返回企业特定的认证声明，通常用于企业内部的高安全性需求。</li>
     *     </ul>
     *   </li>
     *   <li><b>indirect</b>：
     *     <ul>
     *       <li>间接模式，认证器不会生成完整的认证声明，而是通过可信任的第三方（如认证服务提供商）进行验证。返回的认证声明是由第三方生成的。</li>
     *     </ul>
     *   </li>
     *   <li><b>none</b>：
     *     <ul>
     *       <li>无认证模式，不要求认证器生成认证声明。这种模式下，认证器只返回凭证的基本信息，适用于对认证器的真实性要求不高的场景。</li>
     *     </ul>
     *   </li>
     * </ul>
     */
    private AttestationConveyancePreference attestation;

    /**
     * 注册超时时间，可为空，默认60000ms（60s)
     */
    private Long RegistrationTimeout;

    /**
     * 登录超时时间，可为空，默认60000ms(60s)
     */
    private Long AssertionTimeout;

    /**
     * 身份
     */
    @NestedConfigurationProperty
    private Identity identity;

    @NestedConfigurationProperty
    private AuthenticatorSelection authenticatorSelection;

    @Data
    public static class Identity {
        private String id;
        private String name;
    }

    @Data
    public static class AuthenticatorSelection {
        /**
         * residentKey 表示驻留密钥的要求。
         *
         * <ul>
         *   <li><b>required</b>：表示需要认证器支持并存储驻留密钥（resident key）。驻留密钥是存储在认证器中的密钥，可以在没有服务器端存储的情况下进行身份验证。</li>
         *   <li><b>preferred</b>：表示偏好使用驻留密钥，但如果认证器不支持，也可以接受不使用驻留密钥。</li>
         *   <li><b>discouraged</b>：表示不鼓励使用驻留密钥，即使认证器支持也不要求。</li>
         * </ul>
         */
        private ResidentKeyRequirement residentKey;

        /**
         * platform:
         * <ul>
         *   <li><b>platform</b>：表示认证器是与客户端设备直接集成的（如手机或电脑内置的指纹识别器）。</li>
         *   <li><b>cross-platform</b>：表示认证器是独立于客户端设备的（如USB安全密钥或蓝牙设备）。</li>
         *   <li><b>null</b>或不设置：不限制认证器的类型，允许使用任何类型的认证器。</li>
         * </ul>
         */
        private AuthenticatorAttachment authenticatorAttachment;


        /**
         * userVerification 表示用户验证的要求。
         *
         * <ul>
         *   <li><b>required</b>：表示要求用户进行验证（如使用指纹、面部识别或PIN码）。</li>
         *   <li><b>preferred</b>：表示偏好用户进行验证，但如果认证器不支持也可以接受。</li>
         *   <li><b>discouraged</b>：表示不鼓励用户进行验证，即使认证器支持用户验证也不要求。</li>
         * </ul>
         */
        private UserVerificationRequirement userVerification;
    }
}