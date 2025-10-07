package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.dto.UsuarioCriadoDTO;
import com.twentysixcore.chefapi.application.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.Senha;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.domain.event.UsuarioCriado;
import com.twentysixcore.chefapi.application.domain.repository.UsuarioRepository;
import com.twentysixcore.chefapi.infrastructure.event.DomainEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final DomainEventPublisher eventPublisher;

    public CadastrarUsuarioUseCase(UsuarioRepository usuarioRepository, DomainEventPublisher eventPublisher) {
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UsuarioResponseDTO executar(UsuarioCriadoDTO dto) {
        Optional.ofNullable(dto.email()).ifPresentOrElse(email -> {
            if (usuarioRepository.buscarPorEmail(email).isPresent()) {
                throw new IllegalArgumentException("E-mail já cadastrado: " + email);
            }
        }, () -> {
            throw new IllegalArgumentException("E-mail é obrigatório");
        });

        Senha senha = Senha.criar(String.valueOf(dto.senha()));
        Endereco endereco = dto.endereco();
        Usuario usuario = new Usuario(dto.nome(), dto.email(), dto.login(), senha, dto.tipo(), endereco);

        Usuario salvo = usuarioRepository.salvar(usuario);

        eventPublisher.publish(new UsuarioCriado(salvo.getId(), salvo.getEmail()));

        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getLogin(), salvo.getTipo(), salvo.getEndereco(), salvo.getDataUltimaAlteracao());
    }
}
