package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.event.UsuarioCriado;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.CadastrarUsuario;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.infrastructure.event.DomainEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastrarUsuarioUseCase implements CadastrarUsuario {

    private final UsuarioRepository usuarioRepository;
    private final DomainEventPublisher eventPublisher;
    private final SenhaEncoder senhaEncoder;

    public CadastrarUsuarioUseCase(UsuarioRepository usuarioRepository,
                                   DomainEventPublisher eventPublisher,
                                   SenhaEncoder senhaEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
        this.senhaEncoder = senhaEncoder;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO executar(UsuarioRequestDTO dto) {
        // todo: CORRIGIR dependencia com DTO. remover uso do DTO no caso de uso.
        //  mapear os dados do UsuarioRequestDTO para um objeto simples ou receber os dados como parâmetros.
        //  Se a Aplicação conhece o DTO, ela passa a conhecer detalhes do "mundo exterior" (HTTP, JSON).
        //  Isso quebra o princípio fundamental da arquitetura, que é ter o núcleo da aplicação (Domínio e Aplicação) completamente isolado.
        validaEmail(dto);
        validaSenha(dto);

        Usuario usuario = new Usuario(dto.nome(), dto.email(), dto.login(), getHashSenha(dto), dto.tipo(), dto.endereco());
        Usuario salvo = usuarioRepository.salvar(usuario);

        eventPublisher.publish(new UsuarioCriado(salvo.getId(), salvo.getEmail()));

        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getLogin(), salvo.getTipo(), salvo.getEndereco(), salvo.getDataUltimaAlteracao());
    }

    private String getHashSenha(UsuarioRequestDTO dto) {
        return senhaEncoder.encode(dto.senha());
    }

    private void validaEmail(UsuarioRequestDTO dto) {
        Optional.ofNullable(dto.email()).ifPresentOrElse(this::validaEmailExistente, () -> {
            throw new IllegalArgumentException("E-mail é obrigatório");
        });
    }

    private void validaEmailExistente(String email) {
        if (usuarioRepository.buscarPorEmail(email).isPresent())
            throw new IllegalArgumentException("E-mail já cadastrado: " + email);
    }

    private static void validaSenha(UsuarioRequestDTO dto) {
        if (dto.senha() == null || dto.senha().length() < 6)
            throw new IllegalArgumentException("Senha inválida: mínimo 6 caracteres");
    }
}
