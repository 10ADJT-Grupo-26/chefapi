package com.twentysixcore.chefapi.application.ports.inbound.usecase;

import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioResponseDTO;

import java.util.UUID;

public interface BuscarUsuarioPorId {
    UsuarioResponseDTO executar(UUID id);
}
