package com.twentysixcore.chefapi.infrastructure.api.rest.dto;

import jakarta.validation.constraints.*;

public record UsuarioRequestDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String login,
        @NotBlank @Size(min = 6) String senha,
        @NotNull String tipo,
        @NotNull EnderecoDTO endereco
) {}
