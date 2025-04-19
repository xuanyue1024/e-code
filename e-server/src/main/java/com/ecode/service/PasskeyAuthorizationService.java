package com.ecode.service;

import com.ecode.entity.po.CredentialRegistration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;

import java.io.IOException;
import java.util.List;

/**
 * Passkey授权服务
 *
 * @author 竹林听雨
 * @date 2025/04/01
 */
public interface PasskeyAuthorizationService {

        String startPasskeyRegistration(Integer userId) throws JsonProcessingException;

        void finishPasskeyRegistration(Integer userId, String credentialName, String credential) throws IOException, RegistrationFailedException;

        String startPasskeyAssertion(String identifier) throws JsonProcessingException;

        Integer finishPasskeyAssertion(String identifier, PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential) throws IOException, AssertionFailedException;

        List<CredentialRegistration> getPasskeyList(Integer userId);

        void deletePasskey(Integer userId, String id);
}
