package com.twentysixcore.chefapi.application.ports.inbound.dto;

public record EnderecoInput(
        String rua,
        String numero,
        String cidade,
        String cep,
        String uf
) {
}