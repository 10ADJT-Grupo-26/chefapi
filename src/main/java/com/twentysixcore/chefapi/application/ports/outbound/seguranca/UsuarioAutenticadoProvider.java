package com.twentysixcore.chefapi.application.ports.outbound.seguranca;

import java.util.Optional;

public interface UsuarioAutenticadoProvider {
    Optional<String> obterLoginAtual();
}

