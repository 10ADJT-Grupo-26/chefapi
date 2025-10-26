package com.twentysixcore.chefapi.infrastructure.api.rest.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlterarSenhaRequestDTO(
        @NotNull(message = "ID do usuário é obrigatório")
        UUID usuarioId,

        @NotBlank(message = "Senha atual é obrigatória")
        String senhaAtual,

        @NotBlank(message = "Nova senha é obrigatória")
        String novaSenha
) {}
