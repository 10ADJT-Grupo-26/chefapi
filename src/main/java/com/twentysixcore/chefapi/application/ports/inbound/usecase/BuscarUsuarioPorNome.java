package com.twentysixcore.chefapi.application.ports.inbound.usecase;

import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;

public interface BuscarUsuarioPorNome {
    UsuarioOutput executar(String nome);
}
