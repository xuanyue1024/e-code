package com.ecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecode.constant.MessageConstant;
import com.ecode.dto.CredentialRegistration;
import com.ecode.entity.User;
import com.ecode.entity.WebauthnCredential;
import com.ecode.exception.LoginException;
import com.ecode.mapper.UserMapper;
import com.ecode.mapper.WebauthnCredentialMapper;
import com.ecode.service.PasskeyAuthorizationService;
import com.ecode.utils.ByteUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Clock;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class PasskeyAuthorizationServiceImpl implements PasskeyAuthorizationService {
    private final UserMapper userMapper;
    private final RelyingParty relyingParty;
    private final RedisTemplate template;
    private final WebauthnCredentialMapper webauthnCredentialMapper;
    private final String REDIS_PASSKEY_REGISTRATION_KEY = "passkey:registration";
    private final String REDIS_PASSKEY_ASSERTION_KEY = "passkey:assertion";

    /**
     * {@code startPasskeyRegistration(long userID)} 方法用于返回浏览器创建密钥所需的{@code options}。
     * 根据 {@code userId} 获取用户信息后，调用 {@link RelyingParty#startRegistration(StartRegistrationOptions)}
     * 方法，并传入包含 {@link UserIdentity} 信息的 {@code StartRegistrationOptions} 参数，
     * 设置 {@code residentKey} 类型为 {@code REQUIRED} 来强制前端进行生物识别认证以符合 Passkey 的验证需求。
     * 然后，通过调用 {@link PublicKeyCredentialCreationOptions#toJson()} 方法将选项序列化为 JSON 并存入 Redis 中备用。
     * 最后，调用 {@link PublicKeyCredentialCreationOptions#toCredentialsCreateJson()} 方法生成前端所需的选项对象并返回给前端。
     * 前端拿到选项后会调用 {@code navigator.credentials.create} 函数，要求用户身份验证并在成功后返回公钥数据给后端。
     *
     * @param userId 用户的唯一标识符。
     * @return 返回用于创建密钥的选项对象。
     */
    @Override
    public String startPasskeyRegistration(Integer userId) throws JsonProcessingException {
        User user = userMapper.selectById(userId);

        PublicKeyCredentialCreationOptions options = relyingParty.startRegistration(StartRegistrationOptions.builder()
                .user(UserIdentity.builder()
                        .name(user.getEmail())
                        .displayName(user.getUsername())
                        .id(new ByteArray(ByteUtil.intToBytes(user.getId())))
                        .build())
                .timeout(60000L) // 设置超时时间 60 秒
                .authenticatorSelection(AuthenticatorSelectionCriteria.builder()
                        .authenticatorAttachment(AuthenticatorAttachment.PLATFORM) // "preferred"
                        .userVerification(UserVerificationRequirement.PREFERRED)
                        .build())
                .build());

        // 手动设置 attestation
        options = options.toBuilder()
                .attestation(AttestationConveyancePreference.DIRECT) // 认证方式
                .build();
        template.opsForHash().put(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(user.getId()), options.toJson());

        return options.toCredentialsCreateJson();
    }

    /**
     * {@code finishPasskeyRegistration(long userID, String credential)} 方法根据浏览器返回的公钥数据验证 Passkey 创建是否有效。
     * 根据 {@code userID} 从 Redis 中取回存储的序列化 JSON 数据，并调用 {@link PublicKeyCredentialCreationOptions#fromJson(String)}
     * 方法将其反序列化为 {@code PublicKeyCredentialCreationOptions} 对象。然后，调用 {@link RelyingParty#finishRegistration(FinishRegistrationOptions)}
     * 方法，并传入包含 {@code PublicKeyCredentialCreationOptions} 对象的 {@code FinishRegistrationOptions} 参数（用于确认用于验证的密钥），
     * 同时通过调用 {@link PublicKeyCredential#parseRegistrationResponseJson(String)} 方法将前端返回的公钥数据反序列化为所需对象。
     * 最后，调用 {@code storeCredential} 方法将验证结果存入数据库，完成 Passkey 的创建。
     *
     * @param userId 用户的唯一标识符。
     * @param credential 密钥凭证。
     */
    @Override
    public void finishPasskeyRegistration(Integer userId, String credential) throws IOException, RegistrationFailedException {
        var pkc = PublicKeyCredential.parseRegistrationResponseJson(credential);
        PublicKeyCredentialCreationOptions request = PublicKeyCredentialCreationOptions.fromJson((String) template.opsForHash().get(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(userId)));
        System.out.println(relyingParty.getOrigins());
        RegistrationResult result = relyingParty.finishRegistration(FinishRegistrationOptions.builder()
                .request(request)
                .response(pkc)
                .build());

        template.opsForHash().delete(REDIS_PASSKEY_REGISTRATION_KEY, String.valueOf(userId));

        storeCredential(userId, request, result);

    }

    /**
     * {@code AssertionRequest startPasskeyAssertion(String identifier)} 和 {@code long finishPasskeyAssertion(String identifier, String credential)}
     * 方法的操作与上述方法类似，但有一个主要区别：这些方法要求传入的不再是 {@code userID} 而是 {@code identifier}。
     * 这是因为 Passkey 凭据认证具有去用户化的特性。对于其他类型的密钥（例如用于2FA验证的普通密钥），我们通常需要知道所验证的用户信息，
     * 但在用户登录前，对于用于登录用户的 Passkey 来说，我们并不知道所登录用户的信息。因此，在这一步中，我们不必提供用户的 {@code UserIdentity} 信息（如果是其他类型的密钥则仍需提供）。
     * 我们仍然需要一个唯一标识符来确认用于验证的密钥是哪一个，因此引入了 {@code identifier} 机制代替原有的 {@code userID}。
     * 实现上，正是通过调用 {@code HttpServletRequest.getSession().getId()} 方法获取用户 Session 的唯一 ID 作为 Identifier。
     *
     * @param identifier 用户的唯一标识符。
     * @return 返回用于认证的 {@code AssertionRequest} 对象。
     */
    @Override
    public String startPasskeyAssertion(String identifier) throws JsonProcessingException {
        AssertionRequest options = relyingParty.startAssertion(StartAssertionOptions.builder().build());
        template.opsForHash().put(REDIS_PASSKEY_ASSERTION_KEY, identifier, options.toJson());

        return options.toCredentialsGetJson();
    }

    /**
     * 完成 Passkey 认证流程的方法。
     *
     * @param identifier 用户的唯一标识符。
     * @param credential 密钥凭证。
     * @return 返回用于认证的结果。
     */
    @Override
    public Integer finishPasskeyAssertion(String identifier, PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential) throws IOException, AssertionFailedException {
        AssertionRequest request = AssertionRequest.fromJson((String) template.opsForHash().get(REDIS_PASSKEY_ASSERTION_KEY, identifier));
        var pkc = credential;

        AssertionResult result = relyingParty.finishAssertion(FinishAssertionOptions.builder()
                .request(request)
                .response(pkc)
                .build());

        template.opsForHash().delete(REDIS_PASSKEY_ASSERTION_KEY, identifier);

        if (!result.isSuccess()){
            throw new LoginException(MessageConstant.LOGIN_FAILED);
        }

        Integer userId = ByteUtil.bytesToInt(result.getCredential().getUserHandle().getBytes());
        updateCredential(userId, result.getCredential().getCredentialId(), result);

        return userId;
    }

    private void storeCredential(Integer id,
                                 @NotNull PublicKeyCredentialCreationOptions request,
                                 @NotNull RegistrationResult result) {

        webauthnCredentialMapper.insert(fromFinishPasskeyRegistration(id, request, result));
    }

    private void updateCredential(Integer id,
                                  @NotNull ByteArray credentialId,
                                  @NotNull AssertionResult result) {
        WebauthnCredential webauthnCredential = webauthnCredentialMapper.selectList(new LambdaQueryWrapper<WebauthnCredential>().eq(WebauthnCredential::getUserId, id)).stream()
                .filter(wc -> credentialId.equals(wc.getCredentialRegistration().getCredential().getCredentialId()))
                .findAny()
                .orElseThrow();

        webauthnCredential.getCredentialRegistration().setCredential(
                webauthnCredential.getCredentialRegistration().getCredential().toBuilder().signatureCount(result.getSignatureCount()).build()
        );

        //更新，先删除后插入
        webauthnCredentialMapper.deleteById(webauthnCredential.getId());
        webauthnCredentialMapper.insert(webauthnCredential);
    }

    @NotNull
    private static WebauthnCredential fromFinishPasskeyRegistration(Integer id,
                                                                          PublicKeyCredentialCreationOptions request,
                                                                          RegistrationResult result) {
        return new WebauthnCredential(
                null,
                id,
                CredentialRegistration.builder()
                        .userIdentity(request.getUser())
                        .transports(result.getKeyId().getTransports().orElseGet(TreeSet::new))
                        .registration(Clock.systemUTC().instant())
                        .credential(RegisteredCredential.builder()
                                .credentialId(result.getKeyId().getId())
                                .userHandle(request.getUser().getId())
                                .publicKeyCose(result.getPublicKeyCose())
                                .signatureCount(result.getSignatureCount())
                                .build())
                        .build()
        );
    }
}
