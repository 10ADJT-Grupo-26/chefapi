package com.twentysixcore.chefapi.application.ports.outbound.seguranca;

public interface SenhaEncoder {
    String encode(CharSequence rawPassword);
    boolean verifica(CharSequence rawPassword, String encodedPassword);
}
