package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.mapper.UsuarioApplicationMapper;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.BuscarUsuarioPorNome;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BuscarUsuarioPorNomeUseCase implements BuscarUsuarioPorNome {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioApplicationMapper mapper;

    public BuscarUsuarioPorNomeUseCase(UsuarioRepository usuarioRepository, UsuarioApplicationMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioOutput executar(String nome) {
        Optional<Usuario> optUsuario = usuarioRepository.buscarPorNome(nome);
        if (optUsuario.isEmpty())
            throw new NoSuchElementException("Nenhum usuário encontrado com esse nome: " + nome);

        return mapper.toOutput(optUsuario.get());
    }
}
