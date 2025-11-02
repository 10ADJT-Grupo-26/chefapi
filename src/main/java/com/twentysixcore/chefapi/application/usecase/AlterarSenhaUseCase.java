package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.inbound.AlterarSenha;
import com.twentysixcore.chefapi.application.ports.inbound.dto.AlterarSenhaInput;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AlterarSenhaUseCase implements AlterarSenha {

    private final UsuarioRepository repository;
    private final SenhaEncoder senhaEncoder;

    public AlterarSenhaUseCase(UsuarioRepository repository, SenhaEncoder senhaEncoder) {
        this.repository = repository;
        this.senhaEncoder = senhaEncoder;
    }

    @Override
    public void executar(AlterarSenhaInput input) {
        Usuario usuario = repository.buscarPorId(input.usuarioId())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (!senhaEncoder.verifica(input.senhaAtual(), usuario.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }

        String novaSenhaHash = senhaEncoder.encode(input.novaSenha());
        usuario.alterarSenha(novaSenhaHash);

        repository.salvar(usuario);
    }
}

