package com.ecode.service.login.webauthn;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ecode.entity.CredentialRegistration;
import com.ecode.entity.User;
import com.ecode.entity.WebauthnCredential;
import com.ecode.mapper.UserMapper;
import com.ecode.mapper.WebauthnCredentialMapper;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 凭据存储库service,实现webauthn
 *
 * @author 竹林听雨
 * @date 2025/04/04
 */
@Component
public class CredentialRepositoryImpl  implements CredentialRepository {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WebauthnCredentialMapper webauthnCredentialMapper;

    /**
     * 根据用户名获取凭证信息
     *
     * @param username 用户名(邮箱)
     * @return set<公钥凭据描述符>
     */
    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        //根据用户id获取WebauthnCredential，随后获取到Set<PublicKeyCredentialDescriptor>
        return getRegistrationsByEmail(username).stream()
                .map(cr -> PublicKeyCredentialDescriptor.builder()
                    .id(cr.getCredential().getCredentialId())
                    .transports(cr.getTransports())
                    .build()
                )
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * 根据用户名获取用户句柄
     *
     * @param username 用户名(邮箱)
     * @return 可选<字节数组>
     */
    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return getRegistrationsByEmail(username).stream()
                .findAny()
                .map(cr -> cr.getUserIdentity().getId());
    }

    /**
     * 获取用户句柄（UserHandle)获取用户名
     *
     * @param userHandle 用户句柄
     * @return 可选<字符串>
     */
    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        return getRegistrationsByUserHandle(userHandle).stream()
                .findAny()
                .map(CredentialRegistration::getUsername);
    }

    /**
     * 根据凭证 ID 和 UserHandle 获取单个凭证信息
     *
     * @param credentialId 凭证id
     * @param userHandle   用户句柄
     * @return 可选<注册凭据>
     */
    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        Optional<CredentialRegistration> registrationMaybe = webauthnCredentialMapper.selectList(null).stream()
                .map(WebauthnCredential::getCredentialRegistration)
                .filter(cr -> cr.getCredential().getCredentialId().equals(credentialId))
                .findAny();

        return registrationMaybe.map(cr -> RegisteredCredential.builder()
                    .credentialId(cr.getCredential().getCredentialId())
                    .userHandle(cr.getCredential().getUserHandle())
                    .publicKeyCose(cr.getCredential().getPublicKeyCose())
                    .signatureCount(cr.getCredential().getSignatureCount())
                    .build()
                );
    }

    /**
     * 根据凭证 ID 获取多个凭证信息
     *
     * @param credentialId 证书标识
     * @return 设置<注册凭据>
     */
    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        return webauthnCredentialMapper.selectList(null).stream()
                .map(WebauthnCredential::getCredentialRegistration)
                .filter(cr -> cr.getCredential().getCredentialId().equals(credentialId))
                .map(cr -> RegisteredCredential.builder()
                        .credentialId(cr.getCredential().getCredentialId())
                        .userHandle(cr.getCredential().getUserHandle())
                        .publicKeyCose(cr.getCredential().getPublicKeyCose())
                        .signatureCount(cr.getCredential().getSignatureCount())
                        .build()
                ).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * 通过电子邮件获取用户id
     *
     * @param email 电子邮件
     * @return 整数
     */
    private Integer getUserIdByEmail(String email){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email);

        return userMapper.selectOne(wrapper).getId();
    }

    /**
     * 通过电子邮件获取Registrations
     *
     * @param email 电子邮件
     * @return 集合<凭据注册>
     */
    private Collection<CredentialRegistration> getRegistrationsByEmail(String email){
        return getRegistrationsByUserId(getUserIdByEmail(email));
    }

    /**
     * 通过用户id获取Registrations
     *
     * @param userId 用户id
     * @return 集合<凭据注册>
     */
    private Collection<CredentialRegistration> getRegistrationsByUserId(Integer userId){
        return webauthnCredentialMapper.selectList(
                new LambdaQueryWrapper<WebauthnCredential>()
                        .eq(WebauthnCredential::getUserId, userId)
        ).stream().map(WebauthnCredential::getCredentialRegistration).collect(Collectors.toList());
    }
    /**
     * 通过用户句柄获取Registrations
     *
     * @param userHandle 用户句柄
     * @return 集合<凭据注册>
     */
    private Collection<CredentialRegistration> getRegistrationsByUserHandle(ByteArray userHandle){
        return webauthnCredentialMapper.selectList(null).stream()
                .map(WebauthnCredential::getCredentialRegistration)
                .filter(cr -> cr.getUserIdentity().getId().equals(userHandle))
                .collect(Collectors.toList());
    }
}
