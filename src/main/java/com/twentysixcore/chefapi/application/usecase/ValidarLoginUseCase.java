package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.mapper.UsuarioApplicationMapper;
import com.twentysixcore.chefapi.application.ports.inbound.ValidarLogin;
import com.twentysixcore.chefapi.application.ports.inbound.dto.LoginInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import com.twentysixcore.chefapi.infrastructure.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ValidarLoginUseCase implements ValidarLogin {

    private final UsuarioRepository repository;
    private final SenhaEncoder senhaEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioApplicationMapper mapper;

    public ValidarLoginUseCase(UsuarioRepository repository,
                               SenhaEncoder senhaEncoder,
                               JwtTokenProvider jwtTokenProvider,
                               UsuarioApplicationMapper mapper) {
        this.repository = repository;
        this.senhaEncoder = senhaEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mapper = mapper;
    }

    @Override
    public UsuarioOutput executar(LoginInput input) {
        Usuario usuario = repository.buscarPorLogin(input.login())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (!senhaEncoder.verifica(input.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        String token = jwtTokenProvider.gerarToken(usuario.getLogin());
        return mapper.toOutput(usuario, token);
    }
}
