package com.ecode.config;

import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class WebauthnConfiguration {

    private final CredentialRepository credentialRepository;
    @Value("${ecode.webauthn.relying-party.id}")
    private String relyingPartyId;
    @Value("${ecode.webauthn.relying-party.name}")
    private String relyingPartyName;

    @Bean
    public RelyingParty relyingParty() {
        RelyingPartyIdentity rpIdentity = RelyingPartyIdentity.builder()
                .id(relyingPartyId)
                .name(relyingPartyName)
                .build();

        return RelyingParty.builder()
                .identity(rpIdentity)
                .credentialRepository(credentialRepository)
                .build();
    }

}