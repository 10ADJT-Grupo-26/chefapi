package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.BuscarUsuarioPorId;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarUsuarioPorIdUseCase implements BuscarUsuarioPorId {

    private final UsuarioRepository usuarioRepository;

    public BuscarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioResponseDTO executar(UUID id) {
        Optional<Usuario> optUsuario = usuarioRepository.buscarPorId(id);
        if (optUsuario.isEmpty()) {
            throw new IllegalArgumentException("Usuário não existe");
        }
        Usuario usuario = optUsuario.get();
        return new UsuarioResponseDTO(
            usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getLogin(), usuario.getTipo(),
                usuario.getEndereco(), usuario.getDataUltimaAlteracao()
        );
    }
}
