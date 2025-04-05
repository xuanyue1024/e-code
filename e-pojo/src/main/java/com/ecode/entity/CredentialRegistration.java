package com.ecode.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.AuthenticatorTransport;
import com.yubico.webauthn.data.UserIdentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.SortedSet;

/**
 * 数据表webauthn_credential实体成员CredentialRegistration实体
 *
 * @author 竹林听雨
 * @date 2025/04/04
 */
@Builder
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CredentialRegistration implements Serializable {

    private static final long serialVersionUID = -7806476843753634174L;

    /*
    * com.yubico.webauthn.data.UserIdentity userIdentity存储用户标识
    * 由 String name, String displayName, ByteArray id 三部分组成
    * 只有 id 字段作为唯一标识符标识唯一用户
    * name 和 displayName 则只是为用户提供人类可读的文本信息用以标识该账户的名称；
    * */
    @NotNull
    UserIdentity userIdentity;
    /*
    * String credentialNickname，该凭据的昵称，方便用户识别，也可不填（Nullable）
    * */
    @Nullable
    String credentialNickname;

    /*
    * SortedSet<com.yubico.webauthn.data.AuthenticatorTransport> transports
    * 该凭据支持的传输方式，例如 USB, BLE, NFC 等；
    *
    * */
    @NotNull
    SortedSet<@NotNull AuthenticatorTransport> transports;
    /*
    * com.yubico.webauthn.RegisteredCredential credential
    * 凭证详细数据，包括凭证 ID，凭证对应的用户 ID，凭证公钥，签名计数，备份信息等。
    * 该信息由浏览器生成并发回到服务端
    * */
    @NotNull
    RegisteredCredential credential;

    /*
    * Object attestationMetadata，自定义元数据， 可空（Nullable）
    * */
    @Nullable
    Object attestationMetadata;
    /*
    * 凭据的注册时间
    * */
    @NotNull
    private LocalDateTime registration;


    @JsonIgnore
    public String getUsername() {
        return userIdentity.getName();
    }


}
