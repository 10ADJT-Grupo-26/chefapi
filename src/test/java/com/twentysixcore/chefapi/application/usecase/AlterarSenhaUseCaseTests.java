package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.inbound.dto.AlterarSenhaInput;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlterarSenhaUseCaseTests {

    private UsuarioRepository repository;
    private SenhaEncoder senhaEncoder;
    private AlterarSenhaUseCase useCase;

    private UUID usuarioId;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        repository = mock(UsuarioRepository.class);
        senhaEncoder = mock(SenhaEncoder.class);
        useCase = new AlterarSenhaUseCase(repository, senhaEncoder);

        usuarioId = UUID.randomUUID();
        usuario = new Usuario("Joao", "joao@teste.com", "joao", "hashAntigo", null, null);
    }

    @Test
    void deveAlterarSenhaComSucesso() {
        when(repository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(senhaEncoder.verifica("senhaAtual", "hashAntigo")).thenReturn(true);
        when(senhaEncoder.encode("novaSenha")).thenReturn("hashNovo");

        AlterarSenhaInput input = new AlterarSenhaInput(usuarioId, "senhaAtual", "novaSenha");

        useCase.executar(input);

        assertEquals("hashNovo", usuario.getSenha());
        verify(repository).salvar(usuario);
    }

    @Test
    void deveLancarSeUsuarioNaoExistir() {
        when(repository.buscarPorId(usuarioId)).thenReturn(Optional.empty());

        AlterarSenhaInput input = new AlterarSenhaInput(usuarioId, "senhaAtual", "novaSenha");

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> useCase.executar(input));
        assertEquals("Usuário não encontrado", ex.getMessage());
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveLancarSeSenhaAtualEstiverIncorreta() {
        when(repository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(senhaEncoder.verifica("senhaAtualErrada", "hashAntigo")).thenReturn(false);

        AlterarSenhaInput input = new AlterarSenhaInput(usuarioId, "senhaAtualErrada", "novaSenha");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> useCase.executar(input));
        assertEquals("Senha atual incorreta", ex.getMessage());
        verify(repository, never()).salvar(any());
    }
}

