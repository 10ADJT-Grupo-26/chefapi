package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.event.UsuarioCriado;
import com.twentysixcore.chefapi.application.mapper.UsuarioApplicationMapper;
import com.twentysixcore.chefapi.application.ports.inbound.dto.CadastrarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.EnderecoInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import com.twentysixcore.chefapi.infrastructure.event.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CadastrarUsuarioUseCaseTests {

    private UsuarioRepository usuarioRepository;
    private DomainEventPublisher eventPublisher;
    private SenhaEncoder senhaEncoder;
    private UsuarioApplicationMapper mapper;
    private CadastrarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        eventPublisher = mock(DomainEventPublisher.class);
        senhaEncoder = mock(SenhaEncoder.class);
        mapper = mock(UsuarioApplicationMapper.class);
        useCase = new CadastrarUsuarioUseCase(usuarioRepository, eventPublisher, senhaEncoder, mapper);
    }

    private CadastrarUsuarioInput criarInputValido() {
        var enderecoInput = new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG");
        return new CadastrarUsuarioInput("João", "joao@example.com", "joao123", "123456", "CLIENTE", enderecoInput);
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        var input = criarInputValido();
        var endereco = new Endereco("Rua A", "123", "Cidade X", "12345-000", "MG");
        var usuarioSalvo = new Usuario("João", "joao@example.com", "joao123", "hash123", TipoUsuario.CLIENTE, endereco);
        var outputEsperado = new UsuarioOutput(usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getEmail(),
                usuarioSalvo.getLogin(), usuarioSalvo.getTipo().name(),
                new UsuarioOutput.EnderecoOutput("Rua A", "123", "Cidade X", "12345-000", "MG"),
                usuarioSalvo.getDataUltimaAlteracao());

        when(usuarioRepository.buscarPorEmail("joao@example.com")).thenReturn(Optional.empty());
        when(senhaEncoder.encode("123456")).thenReturn("hash123");
        when(usuarioRepository.salvar(any())).thenReturn(usuarioSalvo);
        when(mapper.toOutput(usuarioSalvo)).thenReturn(outputEsperado);

        var resultado = useCase.executar(input);

        assertEquals(outputEsperado, resultado);
        verify(usuarioRepository).salvar(any(Usuario.class));

        ArgumentCaptor<UsuarioCriado> captor = ArgumentCaptor.forClass(UsuarioCriado.class);
        verify(eventPublisher).publish(captor.capture());

        UsuarioCriado evento = captor.getValue();
        assertEquals(usuarioSalvo.getId(), evento.getUsuarioId());
        assertEquals(usuarioSalvo.getEmail(), evento.getEmail());
        assertNotNull(evento.getDataEvento());
    }

    @Test
    void deveLancarErroQuandoEmailJaExiste() {
        var input = criarInputValido();
        when(usuarioRepository.buscarPorEmail("joao@example.com"))
                .thenReturn(Optional.of(new Usuario()));

        var exception = assertThrows(IllegalArgumentException.class, () -> useCase.executar(input));

        assertEquals("E-mail já cadastrado: joao@example.com", exception.getMessage());
        verify(usuarioRepository, never()).salvar(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarErroQuandoEmailEhNulo() {
        var enderecoInput = new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG");
        var input = new CadastrarUsuarioInput("João", null, "joao123", "123456", "CLIENTE", enderecoInput);

        var exception = assertThrows(IllegalArgumentException.class, () -> useCase.executar(input));

        assertEquals("E-mail é obrigatório", exception.getMessage());
        verify(usuarioRepository, never()).salvar(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveLancarErroQuandoSenhaInvalida() {
        var enderecoInput = new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG");
        var input = new CadastrarUsuarioInput("João", "joao@example.com", "joao123", "123", "CLIENTE", enderecoInput);

        var exception = assertThrows(IllegalArgumentException.class, () -> useCase.executar(input));

        assertEquals("Senha inválida: mínimo 6 caracteres", exception.getMessage());
        verify(usuarioRepository, never()).salvar(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void deveGerarHashDaSenhaCorretamente() {
        when(senhaEncoder.encode("123456")).thenReturn("hashXYZ");

        var input = criarInputValido();
        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toOutput(any())).thenReturn(mock(UsuarioOutput.class));

        useCase.executar(input);

        verify(senhaEncoder).encode("123456");
    }
}


