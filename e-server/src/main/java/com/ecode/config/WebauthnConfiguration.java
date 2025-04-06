package com.ecode.config;

import com.ecode.properties.WebAuthnProperties;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
//https://developers.yubico.com/java-webauthn-server/JavaDoc/webauthn-server-core/2.5.0/com/yubico/webauthn/RelyingParty.RelyingPartyBuilder.html
public class WebauthnConfiguration {

    private final CredentialRepository credentialRepository;

    private final WebAuthnProperties webAuthnProperties;

    @Bean
    public RelyingParty relyingParty() {
        RelyingPartyIdentity rpIdentity = RelyingPartyIdentity.builder()
                .id(webAuthnProperties.getIdentity().getId())
                .name(webAuthnProperties.getIdentity().getName())
                .build();

        return RelyingParty.builder()
                .identity(rpIdentity)
                .credentialRepository(credentialRepository)
                .origins(webAuthnProperties.getAllowedOrigins())
                .attestationConveyancePreference(webAuthnProperties.getAttestation())
                .build();
    }

}