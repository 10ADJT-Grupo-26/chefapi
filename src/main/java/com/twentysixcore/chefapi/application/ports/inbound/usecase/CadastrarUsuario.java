package com.twentysixcore.chefapi.application.ports.inbound.usecase;

import com.twentysixcore.chefapi.application.ports.inbound.dto.CadastrarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;

public interface CadastrarUsuario {
    UsuarioOutput executar(CadastrarUsuarioInput dto);
}
