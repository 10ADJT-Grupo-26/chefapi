package com.twentysixcore.chefapi.infrastructure.security;

import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptSenhaEncoder implements SenhaEncoder {

    private final BCryptPasswordEncoder encoder;

    public BCryptSenhaEncoder() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean verifica(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
