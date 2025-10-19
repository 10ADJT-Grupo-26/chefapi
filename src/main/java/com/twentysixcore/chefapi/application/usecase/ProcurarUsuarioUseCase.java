package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.infrastructure.event.DomainEventPublisher;
import com.twentysixcore.chefapi.infrastructure.persistence.UsuarioJPARepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Component
@Slf4j
@Service
public class ProcurarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioJPARepository usuarioJPARepository;
    private final DomainEventPublisher eventPublisher;

    public ProcurarUsuarioUseCase(UsuarioRepository usuarioRepository, UsuarioJPARepository usuarioJPARepository, DomainEventPublisher eventPublisher) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioJPARepository = usuarioJPARepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ResponseEntity<List<UsuarioResponseDTO>> executar(String parametroBusca) {
        List<UsuarioResponseDTO> listaUsuarios = usuarioRepository.listarTodosPorParametro(parametroBusca);
        return ResponseEntity.status(HttpStatus.CREATED).body(listaUsuarios);
    }
}
