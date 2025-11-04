package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.event.UsuarioCriado;
import com.twentysixcore.chefapi.application.exception.*;
import com.twentysixcore.chefapi.application.mapper.UsuarioApplicationMapper;
import com.twentysixcore.chefapi.application.ports.inbound.dto.CadastrarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.EnderecoInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.outbound.DomainEventPublisher;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.ports.outbound.seguranca.SenhaEncoder;
import com.twentysixcore.chefapi.application.ports.outbound.seguranca.UsuarioAutenticadoProvider;
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
    private UsuarioAutenticadoProvider autenticadoProvider;
    private CadastrarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        eventPublisher = mock(DomainEventPublisher.class);
        senhaEncoder = mock(SenhaEncoder.class);
        mapper = mock(UsuarioApplicationMapper.class);
        autenticadoProvider = mock(UsuarioAutenticadoProvider.class);

        useCase = new CadastrarUsuarioUseCase(
                usuarioRepository,
                eventPublisher,
                senhaEncoder,
                mapper,
                autenticadoProvider
        );
    }

    private CadastrarUsuarioInput criarInputValido() {
        var enderecoInput = new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG");
        return new CadastrarUsuarioInput("João", "joao@example.com", "joao123", "123456", "CLIENTE", enderecoInput);
    }

    private Usuario criarUsuarioSalvo() {
        var endereco = new Endereco("Rua A", "123", "Cidade X", "12345-000", "MG");
        return new Usuario("João", "joao@example.com", "joao123", "hash123", TipoUsuario.CLIENTE, endereco);
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        var input = criarInputValido();
        var usuarioSalvo = criarUsuarioSalvo();
        var outputEsperado = mock(UsuarioOutput.class);

        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.buscarPorLogin(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.salvar(any())).thenReturn(usuarioSalvo);
        when(senhaEncoder.encode(anyString())).thenReturn("hash123");
        when(mapper.toOutput(usuarioSalvo)).thenReturn(outputEsperado);
        when(autenticadoProvider.obterLoginAtual()).thenReturn(Optional.empty()); // Usuário anônimo (cliente público)

        var resultado = useCase.executar(input);

        assertEquals(outputEsperado, resultado);
        verify(usuarioRepository).salvar(any(Usuario.class));

        var captor = ArgumentCaptor.forClass(UsuarioCriado.class);
        verify(eventPublisher).publish(captor.capture());
        assertEquals(usuarioSalvo.getEmail(), captor.getValue().getEmail());
    }

    @Test
    void deveLancarErroQuandoEmailJaExiste() {
        var input = criarInputValido();
        when(usuarioRepository.buscarPorEmail("joao@example.com"))
                .thenReturn(Optional.of(criarUsuarioSalvo()));

        var ex = assertThrows(EmailJaCadastradoException.class, () -> useCase.executar(input));

        assertTrue(ex.getMessage().contains("E-mail já cadastrado"));
        verify(usuarioRepository, never()).salvar(any());
    }

    @Test
    void deveLancarErroQuandoEmailEhNulo() {
        var enderecoInput = new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG");
        var input = new CadastrarUsuarioInput("João", null, "joao123", "123456", "CLIENTE", enderecoInput);

        var ex = assertThrows(EmailObrigatorioException.class, () -> useCase.executar(input));
        assertEquals("E-mail é obrigatório.", ex.getMessage());
        verify(usuarioRepository, never()).salvar(any());
    }

    @Test
    void deveLancarErroQuandoLoginJaExiste() {
        var input = criarInputValido();
        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.buscarPorLogin("joao123")).thenReturn(Optional.of(criarUsuarioSalvo()));

        var ex = assertThrows(LoginJaCadastradoException.class, () -> useCase.executar(input));
        assertTrue(ex.getMessage().contains("Login já cadastrado"));
    }

    @Test
    void deveLancarErroQuandoLoginEhNulo() {
        var endereco = new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG");
        var input = new CadastrarUsuarioInput("João", "joao@example.com", null, "123456", "CLIENTE", endereco);

        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());

        var ex = assertThrows(LoginObrigatorioException.class, () -> useCase.executar(input));
        assertEquals("Login é obrigatório.", ex.getMessage());
    }

    @Test
    void deveLancarErroQuandoSenhaInvalida() {
        var enderecoInput = new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG");
        var input = new CadastrarUsuarioInput("João", "joao@example.com", "joao123", "123", "CLIENTE", enderecoInput);

        var ex = assertThrows(SenhaInvalidaException.class, () -> useCase.executar(input));
        assertTrue(ex.getMessage().contains("Senha inválida"));
    }

    @Test
    void devePermitirClienteAnonimoCriarContaDeCliente() {
        var input = criarInputValido();
        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.buscarPorLogin(anyString())).thenReturn(Optional.empty());
        when(senhaEncoder.encode(anyString())).thenReturn("hash123");
        when(usuarioRepository.salvar(any())).thenReturn(criarUsuarioSalvo());
        when(mapper.toOutput(any())).thenReturn(mock(UsuarioOutput.class));
        when(autenticadoProvider.obterLoginAtual()).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> useCase.executar(input));
    }

    @Test
    void deveNegarClienteAnonimoCriarContaDeAdmin() {
        var endereco = new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG");
        var input = new CadastrarUsuarioInput("João", "joao@example.com", "joao123", "123456", "ADMIN", endereco);

        when(autenticadoProvider.obterLoginAtual()).thenReturn(Optional.empty());

        var ex = assertThrows(PermissaoNegadaException.class, () -> useCase.executar(input));
        assertTrue(ex.getMessage().contains("Apenas usuários CLIENTE podem se registrar"));
    }

    @Test
    void devePermitirAdminCriarQualquerUsuario() {
        var input = criarInputValido();
        var admin = new Usuario("Admin", "admin@x.com", "admin", "hash", TipoUsuario.ADMIN,
                new Endereco("Rua", "1", "Cidade", "00000-000", "MG"));

        when(autenticadoProvider.obterLoginAtual()).thenReturn(Optional.of("admin"));
        when(usuarioRepository.buscarPorLogin("admin")).thenReturn(Optional.of(admin));
        when(usuarioRepository.buscarPorEmail(any())).thenReturn(Optional.empty());
        when(usuarioRepository.salvar(any())).thenReturn(criarUsuarioSalvo());
        when(mapper.toOutput(any())).thenReturn(mock(UsuarioOutput.class));
        when(senhaEncoder.encode(any())).thenReturn("hash");

        assertDoesNotThrow(() -> useCase.executar(input));
    }

    @Test
    void deveNegarClienteCriandoAdmin() {
        var input = new CadastrarUsuarioInput("João", "joao@example.com", "joao123", "123456", "ADMIN",
                new EnderecoInput("Rua A", "123", "Cidade X", "12345-000", "MG"));

        var cliente = new Usuario("Maria", "maria@x.com", "maria", "hash", TipoUsuario.CLIENTE,
                new Endereco("Rua", "1", "Cidade", "00000-000", "MG"));

        when(autenticadoProvider.obterLoginAtual()).thenReturn(Optional.of("maria"));
        when(usuarioRepository.buscarPorLogin("maria")).thenReturn(Optional.of(cliente));

        var ex = assertThrows(PermissaoNegadaException.class, () -> useCase.executar(input));
        assertTrue(ex.getMessage().contains("Usuários CLIENTE só podem criar novos usuários CLIENTE"));
    }

    @Test
    void deveGerarHashDaSenhaCorretamente() {
        var input = criarInputValido();
        when(usuarioRepository.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.buscarPorLogin(anyString())).thenReturn(Optional.empty());
        when(senhaEncoder.encode("123456")).thenReturn("hashXYZ");
        when(usuarioRepository.salvar(any())).thenReturn(criarUsuarioSalvo());
        when(mapper.toOutput(any())).thenReturn(mock(UsuarioOutput.class));
        when(autenticadoProvider.obterLoginAtual()).thenReturn(Optional.empty());

        useCase.executar(input);

        verify(senhaEncoder).encode("123456");
    }
}
