package com.twentysixcore.chefapi.application.ports.inbound;

import com.twentysixcore.chefapi.application.ports.inbound.dto.AlterarSenhaInput;

public interface AlterarSenha {
    void executar(AlterarSenhaInput dto);
}
