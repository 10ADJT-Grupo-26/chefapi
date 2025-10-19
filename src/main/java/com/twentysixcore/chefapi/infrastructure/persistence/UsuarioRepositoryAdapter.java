package com.twentysixcore.chefapi.infrastructure.persistence;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.domain.repository.UsuarioRepository;
import com.twentysixcore.chefapi.application.dto.UsuarioResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJPARepository usuarioRepository;

    public UsuarioRepositoryAdapter(UsuarioJPARepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity e = toEntity(usuario);
        e = usuarioRepository.save(e);
        return toDomain(e);
    }
    @Override
    public List<UsuarioResponseDTO> listarTodosPorParametro(String parametroBusca) {
        List<UsuarioEntity> entities = usuarioRepository.listarTodosPorParametro(parametroBusca);

        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    private UsuarioResponseDTO toResponseDTO(UsuarioEntity entity) {
        return new UsuarioResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                TipoUsuario.valueOf(entity.getTipo()), // Conversão aqui
                new Endereco(
                        entity.getRua(),
                        entity.getNumero(),
                        entity.getCidade(),
                        entity.getCep(),
                        entity.getUf()
                ),
                entity.getDataUltimaAlteracao()
        );
    }
    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(this::toDomain);
    }

    private Usuario toDomain(UsuarioEntity e) {
        Endereco end = new Endereco(e.getRua(), e.getNumero(), e.getCidade(), e.getCep(), e.getUf());
        Usuario u = new Usuario();
        u.setId(e.getId());
        u.setNome(e.getNome());
        u.setEmail(e.getEmail());
        u.setLogin(e.getLogin());
        u.setPasswordHash(e.getPasswordHash());
        u.setTipo(TipoUsuario.valueOf(e.getTipo()));
        u.setEndereco(end);
        u.setDataUltimaAlteracao(e.getDataUltimaAlteracao());
        return u;
    }

    private UsuarioEntity toEntity(Usuario u) {
        UsuarioEntity e = new UsuarioEntity();
        e.setId(u.getId() != null ? u.getId() : UUID.randomUUID());
        e.setNome(u.getNome());
        e.setEmail(u.getEmail());
        e.setLogin(u.getLogin());
        e.setPasswordHash(u.getPasswordHash());
        e.setTipo(u.getTipo() != null ? u.getTipo().name() : null);
        if (u.getEndereco() != null) {
            e.setRua(u.getEndereco().getRua());
            e.setNumero(u.getEndereco().getNumero());
            e.setCidade(u.getEndereco().getCidade());
            e.setCep(u.getEndereco().getCep());
            e.setUf(u.getEndereco().getUf());
        }
        e.setDataUltimaAlteracao(u.getDataUltimaAlteracao() != null ? u.getDataUltimaAlteracao() : java.time.OffsetDateTime.now());
        return e;
    }
}
