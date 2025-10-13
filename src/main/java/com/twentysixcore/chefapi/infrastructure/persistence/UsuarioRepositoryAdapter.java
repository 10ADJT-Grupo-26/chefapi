package com.twentysixcore.chefapi.infrastructure.persistence;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJPARepository repo;

    public UsuarioRepositoryAdapter(UsuarioJPARepository repo) {
        this.repo = repo;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity e = toEntity(usuario);
        e = repo.save(e);
        return toDomain(e);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repo.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existePorId(UUID id) {
        return repo.existsById(id);
    }

    @Override
    public void deletarPorId(UUID id) {
        repo.deleteById(id);
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
