package com.twentysixcore.chefapi.application.ports.inbound.usecase;

import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;

import java.util.UUID;

public interface BuscarUsuarioPorId {
    UsuarioOutput executar(UUID id);
}
