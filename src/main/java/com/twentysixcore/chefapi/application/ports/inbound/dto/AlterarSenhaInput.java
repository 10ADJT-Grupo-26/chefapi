package com.twentysixcore.chefapi.application.ports.inbound.dto;

import java.util.UUID;

public record AlterarSenhaInput(
        UUID usuarioId,
        String senhaAtual,
        String novaSenha
) {

}
