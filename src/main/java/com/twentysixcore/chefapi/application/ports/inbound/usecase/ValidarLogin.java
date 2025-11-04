package com.twentysixcore.chefapi.application.ports.inbound.usecase;

import com.twentysixcore.chefapi.application.ports.inbound.dto.LoginInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;

public interface ValidarLogin {
    UsuarioOutput executar(LoginInput input);
}
