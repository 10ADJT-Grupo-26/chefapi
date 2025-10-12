package com.twentysixcore.chefapi.application.ports.outbound.repository;

import com.twentysixcore.chefapi.application.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);

}
