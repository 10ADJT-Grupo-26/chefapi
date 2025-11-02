package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.inbound.ValidarLogin;
import com.twentysixcore.chefapi.application.ports.inbound.dto.LoginInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ValidarLoginUseCase implements ValidarLogin {

    private final UsuarioRepository repository;
    private final SenhaEncoder senhaEncoder;

    public ValidarLoginUseCase(UsuarioRepository repository, SenhaEncoder senhaEncoder) {
        this.repository = repository;
        this.senhaEncoder = senhaEncoder;
    }

    @Override
    public UsuarioOutput executar(LoginInput input) {
        Usuario usuario = repository.buscarPorLogin(input.login())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (!senhaEncoder.verifica(input.senha(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        return new UsuarioOutput(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                usuario.getTipo().name(),
                new UsuarioOutput.EnderecoOutput(
                        usuario.getEndereco().getRua(),
                        usuario.getEndereco().getNumero(),
                        usuario.getEndereco().getCidade(),
                        usuario.getEndereco().getCep(),
                        usuario.getEndereco().getUf()
                ),
                usuario.getDataUltimaAlteracao()
        );
    }
}
