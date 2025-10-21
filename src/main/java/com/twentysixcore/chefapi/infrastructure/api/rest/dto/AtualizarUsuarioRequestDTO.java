package com.twentysixcore.chefapi.infrastructure.api.rest.dto;

import jakarta.validation.constraints.Email;

public record AtualizarUsuarioRequestDTO(
        String nome,
        @Email String email,
        String login,
        String tipo,
        EnderecoDTO endereco
) {}
