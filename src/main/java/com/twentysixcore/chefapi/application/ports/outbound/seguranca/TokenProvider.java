package com.twentysixcore.chefapi.application.ports.outbound.seguranca;

public interface TokenProvider {
    String gerarToken(String login);
}
