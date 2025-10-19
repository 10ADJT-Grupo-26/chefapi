package com.twentysixcore.chefapi.infrastructure.api.rest.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        String login,
        String  tipo,
        EnderecoDTO endereco,
        OffsetDateTime dataUltimaAlteracao
) {
}
