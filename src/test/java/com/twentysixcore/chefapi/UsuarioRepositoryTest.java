package com.twentysixcore.chefapi;

import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import com.twentysixcore.chefapi.application.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.infrastructure.persistence.UsuarioEntity;
import com.twentysixcore.chefapi.infrastructure.persistence.UsuarioJPARepository;
import com.twentysixcore.chefapi.infrastructure.persistence.UsuarioRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ActiveProfiles("test")
@Import(UsuarioRepositoryAdapter.class) // Importa o adapter se necessário
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioJPARepository usuarioJPARepository;

    @Autowired
    private UsuarioRepositoryAdapter usuarioRepositoryAdapter;

    @BeforeEach
    void setUp() {
        // Limpa dados anteriores
        usuarioJPARepository.deleteAll();

        // Cria usuários de teste
        UsuarioEntity usuario1 = new UsuarioEntity();
        usuario1.setId(UUID.randomUUID());
        usuario1.setNome("Lucas Silva");
        usuario1.setEmail("lucas@email.com");
        usuario1.setLogin("lucas.silva");
        usuario1.setPasswordHash("hash123");
        usuario1.setTipo(TipoUsuario.CLIENTE.name());
        usuario1.setRua("Rua A");
        usuario1.setNumero("123");
        usuario1.setCidade("São Paulo");
        usuario1.setCep("01234-567");
        usuario1.setUf("SP");
        usuario1.setDataUltimaAlteracao(OffsetDateTime.now());

        UsuarioEntity usuario2 = new UsuarioEntity();
        usuario2.setId(UUID.randomUUID());
        usuario2.setNome("Maria Santos");
        usuario2.setEmail("maria@email.com");
        usuario2.setLogin("maria.santos");
        usuario2.setPasswordHash("hash456");
        usuario2.setTipo(TipoUsuario.DONO_RESTAURANTE.name());
        usuario2.setRua("Rua B");
        usuario2.setNumero("456");
        usuario2.setCidade("Rio de Janeiro");
        usuario2.setCep("98765-432");
        usuario2.setUf("RJ");
        usuario2.setDataUltimaAlteracao(OffsetDateTime.now());

        usuarioJPARepository.save(usuario1);
        usuarioJPARepository.save(usuario2);
    }
    @Test
    public void testProcurarUsuario() {
        String parametroTeste = "Lucas";

        // Act
        List<UsuarioResponseDTO> results = usuarioRepositoryAdapter.listarTodosPorParametro(parametroTeste);

        // Assert
        System.out.println("Resultados encontrados: " + results.size());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).nome()).isEqualTo("Lucas Silva");
        assertThat(results.get(0).email()).isEqualTo("lucas@email.com");
    }
}