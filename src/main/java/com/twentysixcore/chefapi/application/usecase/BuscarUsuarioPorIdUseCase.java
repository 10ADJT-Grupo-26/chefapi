package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.mapper.UsuarioApplicationMapper;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.BuscarUsuarioPorId;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarUsuarioPorIdUseCase implements BuscarUsuarioPorId {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioApplicationMapper mapper;

    public BuscarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository, UsuarioApplicationMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioOutput executar(UUID id) {
        Optional<Usuario> optUsuario = usuarioRepository.buscarPorId(id);
        if (optUsuario.isEmpty()) {
            throw new IllegalArgumentException("Usuário não existe");
        }
        return mapper.toOutput(optUsuario.get());
    }
}
