package com.twentysixcore.chefapi.infrastructure.security;

import com.twentysixcore.chefapi.application.ports.outbound.seguranca.UsuarioAutenticadoProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter que implementa UsuarioAutenticadoProvider
 * usando o contexto de segurança do Spring Security.
 */
@Component
public class SpringUsuarioAutenticadoProvider implements UsuarioAutenticadoProvider {

    @Override
    public Optional<String> obterLoginAtual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return Optional.empty();
        }

        return Optional.ofNullable(auth.getName());
    }
}
