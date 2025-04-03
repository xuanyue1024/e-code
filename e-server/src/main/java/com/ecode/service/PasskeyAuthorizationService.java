package com.ecode.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;

import java.io.IOException;

/**
 * Passkey授权服务
 *
 * @author 竹林听雨
 * @date 2025/04/01
 */
public interface PasskeyAuthorizationService {

        PublicKeyCredentialCreationOptions startPasskeyRegistration(Integer userId) throws JsonProcessingException;

        void finishPasskeyRegistration(Integer userId, String credential) throws IOException, RegistrationFailedException;

        AssertionRequest startPasskeyAssertion(String identifier) throws JsonProcessingException;

        Integer finishPasskeyAssertion(String identifier, PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential) throws IOException, AssertionFailedException;

}
