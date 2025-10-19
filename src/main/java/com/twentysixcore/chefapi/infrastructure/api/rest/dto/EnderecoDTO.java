package com.twentysixcore.chefapi.infrastructure.api.rest.dto;

public record EnderecoDTO(
        String rua,
        String numero,
        String cidade,
        String cep,
        String uf
) {}