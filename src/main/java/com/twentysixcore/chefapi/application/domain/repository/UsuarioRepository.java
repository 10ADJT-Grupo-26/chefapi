package com.twentysixcore.chefapi.application.domain.repository;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.dto.UsuarioResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    List<UsuarioResponseDTO> listarTodosPorParametro(String parametroBusca);
}
