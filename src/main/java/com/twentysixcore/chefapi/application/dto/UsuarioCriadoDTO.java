package com.twentysixcore.chefapi.application.dto;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import jakarta.validation.constraints.*;

public record UsuarioCriadoDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String login,
        @NotBlank @Size(min = 6) String senha,
        @NotNull TipoUsuario tipo,
        Endereco endereco
) {}
