package com.twentysixcore.chefapi.application.ports.outbound.repository;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    Optional<Usuario> buscarPorId(UUID id);
    boolean existePorId(UUID id);
    void deletarPorId(UUID id);
    List<UsuarioOutput> listarTodosPorParametro(String parametroBusca);
}
