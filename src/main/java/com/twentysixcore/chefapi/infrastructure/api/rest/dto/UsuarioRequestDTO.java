package com.twentysixcore.chefapi.infrastructure.api.rest.dto;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import jakarta.validation.constraints.*;

public record UsuarioRequestDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String login,
        @NotBlank @Size(min = 6) String senha,
        @NotNull TipoUsuario tipo, // TODO: corrigir - infraestrutura esta com dependencia na camada de dominio
        Endereco endereco // TODO: corrigir - infraestrutura esta com dependencia na camada de dominio
) {}
