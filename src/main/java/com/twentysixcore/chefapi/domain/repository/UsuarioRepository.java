package com.twentysixcore.chefapi.domain.repository;

import com.twentysixcore.chefapi.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);

}
