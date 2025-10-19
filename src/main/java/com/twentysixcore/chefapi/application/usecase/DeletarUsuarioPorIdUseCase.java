package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.ports.inbound.usecase.DeletarUsuarioPorId;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class DeletarUsuarioPorIdUseCase implements DeletarUsuarioPorId {
    private final UsuarioRepository usuarioRepository;

    public DeletarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void executar(UUID id) {
        if (!usuarioRepository.existePorId(id))
            throw new NoSuchElementException("Nenhum usuário encontrado com o id: " + id);

        usuarioRepository.deletarPorId(id);
    }
}