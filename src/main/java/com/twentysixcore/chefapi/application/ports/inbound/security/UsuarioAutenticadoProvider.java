package com.twentysixcore.chefapi.application.ports.inbound.security;

import java.util.Optional;

public interface UsuarioAutenticadoProvider {
    Optional<String> obterLoginAtual();
}

