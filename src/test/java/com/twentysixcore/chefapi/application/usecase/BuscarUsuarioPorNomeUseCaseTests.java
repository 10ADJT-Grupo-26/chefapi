package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.infrastructure.persistence.UsuarioRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Import(BuscarUsuarioPorNomeUseCase.class)
public class BuscarUsuarioPorNomeUseCaseTests {

    @Autowired
    private UsuarioRepositoryAdapter usuarioRepositoryAdapter;

    @BeforeEach
    void setUp() {
        // Limpa dados anteriores
        usuarioRepositoryAdapter.deleteAll();

        // Cria usuários de teste
        Usuario usuario1 = new Usuario();
        usuario1.setId(UUID.randomUUID());
        usuario1.setNome("Lucas Silva");
        usuario1.setEmail("lucas@email.com");
        usuario1.setLogin("lucas.silva");
        usuario1.setSenha("hash123");
        usuario1.setTipo(TipoUsuario.CLIENTE.name());

        usuario1.setEndereco(new Endereco("Rua A", "123", "São Paulo", "01234-567", "SP"));
        usuario1.setDataUltimaAlteracao(OffsetDateTime.now());

        Usuario usuario2 = new Usuario();
        usuario2.setId(UUID.randomUUID());
        usuario2.setNome("Maria Santos");
        usuario2.setEmail("maria@email.com");
        usuario2.setLogin("maria.santos");
        usuario2.setSenha("hash456");
        usuario2.setTipo(TipoUsuario.DONO_RESTAURANTE.name());
        usuario2.setEndereco(new Endereco("Rua B", "456", "Rio de Janeiro", "98765-432", "RJ"));
        usuario2.setDataUltimaAlteracao(OffsetDateTime.now());

        usuarioRepositoryAdapter.salvar(usuario1);
        usuarioRepositoryAdapter.salvar(usuario2);
    }
    @Test
    public void testProcurarUsuario() {
        //mudar aqui para testar segundo usuario
        String parametroTeste = "Lucas";

        // Act
        Optional<Usuario> result = usuarioRepositoryAdapter.buscarPorNome(parametroTeste);

        // Assert
        System.out.println("Resultados encontrados: " + result);
        assertThat(result).isNotEmpty();
        assertThat(result.get().getNome()).isEqualTo("Lucas Silva");
        }
}
