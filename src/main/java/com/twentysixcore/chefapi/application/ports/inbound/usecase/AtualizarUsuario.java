package com.twentysixcore.chefapi.application.ports.inbound.usecase;

import com.twentysixcore.chefapi.application.ports.inbound.dto.AtualizarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;

import java.util.UUID;

public interface AtualizarUsuario {
    UsuarioOutput executar(UUID id, AtualizarUsuarioInput input);
}
