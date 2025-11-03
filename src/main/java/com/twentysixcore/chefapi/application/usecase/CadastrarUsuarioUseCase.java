package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.event.UsuarioCriado;
import com.twentysixcore.chefapi.application.exception.*;
import com.twentysixcore.chefapi.application.mapper.UsuarioApplicationMapper;
import com.twentysixcore.chefapi.application.ports.inbound.dto.CadastrarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.inbound.security.UsuarioAutenticadoProvider;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.CadastrarUsuario;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import com.twentysixcore.chefapi.infrastructure.event.DomainEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastrarUsuarioUseCase implements CadastrarUsuario {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioApplicationMapper mapper;
    private final DomainEventPublisher eventPublisher;
    private final SenhaEncoder senhaEncoder;
    private final UsuarioAutenticadoProvider autenticadoProvider;

    public CadastrarUsuarioUseCase(
            UsuarioRepository usuarioRepository,
            DomainEventPublisher eventPublisher,
            SenhaEncoder senhaEncoder,
            UsuarioApplicationMapper mapper,
            UsuarioAutenticadoProvider autenticadoProvider
    ) {
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
        this.senhaEncoder = senhaEncoder;
        this.mapper = mapper;
        this.autenticadoProvider = autenticadoProvider;
    }

    @Override
    @Transactional
    public UsuarioOutput executar(CadastrarUsuarioInput input) {
        validaEmail(input.email());
        validaLogin(input.login());
        validaSenha(input.senha());
        validarPermissoesParaCriacao(input.tipo());

        Endereco endereco = new Endereco(
                input.endereco().rua(),
                input.endereco().numero(),
                input.endereco().cidade(),
                input.endereco().cep(),
                input.endereco().uf()
        );

        TipoUsuario tipoUsuario = TipoUsuario.valueOf(input.tipo().toUpperCase());

        Usuario usuario = usuarioRepository.salvar(
                new Usuario(
                        input.nome(),
                        input.email(),
                        input.login(),
                        getHashSenha(input.senha()),
                        tipoUsuario,
                        endereco
                )
        );

        eventPublisher.publish(new UsuarioCriado(usuario.getId(), usuario.getEmail()));
        return mapper.toOutput(usuario);
    }

    private String getHashSenha(String senha) {
        return senhaEncoder.encode(senha);
    }

    private void validaEmail(String email) {
        Optional.ofNullable(email).ifPresentOrElse(this::validaEmailExistente, () -> {
            throw new EmailObrigatorioException();
        });
    }

    private void validaEmailExistente(String email) {
        if (usuarioRepository.buscarPorEmail(email).isPresent()) {
            throw new EmailJaCadastradoException(email);
        }
    }

    private void validaLogin(String login) {
        Optional.ofNullable(login).ifPresentOrElse(this::validaLoginExistente, () -> {
            throw new LoginObrigatorioException();
        });
    }

    private void validaLoginExistente(String login) {
        if (usuarioRepository.buscarPorLogin(login).isPresent()) {
            throw new LoginJaCadastradoException(login);
        }
    }

    private static void validaSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new SenhaInvalidaException();
        }
    }

    private void validarPermissoesParaCriacao(String tipoRequisitado) {
        Optional<String> loginAtual = autenticadoProvider.obterLoginAtual();

        if (loginAtual.isEmpty()) {
            if (!tipoRequisitado.equalsIgnoreCase("CLIENTE")) {
                throw new PermissaoNegadaException("Apenas usuários CLIENTE podem se registrar sem autenticação.");
            }
            return;
        }

        Usuario usuarioLogado = usuarioRepository.buscarPorLogin(loginAtual.get())
                .orElseThrow(() -> new PermissaoNegadaException("Usuário autenticado não encontrado."));

        TipoUsuario tipoLogado = usuarioLogado.getTipo();
        TipoUsuario tipoSolicitado = TipoUsuario.valueOf(tipoRequisitado.toUpperCase());

        switch (tipoLogado) {
            case ADMIN:
            case DONO_RESTAURANTE:
                break;
            case CLIENTE:
                if (!tipoSolicitado.equals(TipoUsuario.CLIENTE)) {
                    throw new PermissaoNegadaException("Usuários CLIENTE só podem criar novos usuários CLIENTE.");
                }
                break;
            default:
                throw new PermissaoNegadaException("Tipo de usuário não autorizado.");
        }
    }
}
