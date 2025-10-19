package com.twentysixcore.chefapi.application.ports.inbound.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioOutput(
        UUID id,
        String nome,
        String email,
        String login,
        String tipo,
        EnderecoOutput endereco,
        OffsetDateTime dataUltimaAlteracao
) {
    public record EnderecoOutput(
            String rua,
            String numero,
            String cidade,
            String cep,
            String uf
    ) {}
}