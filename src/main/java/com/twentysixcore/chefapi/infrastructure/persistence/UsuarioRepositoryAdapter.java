package com.twentysixcore.chefapi.infrastructure.persistence;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import com.twentysixcore.chefapi.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJPARepository repo;
    private final UsuarioPersistenceMapper mapper;

    public UsuarioRepositoryAdapter(UsuarioJPARepository repo, UsuarioPersistenceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity e = mapper.toEntity(usuario);
        e = repo.save(e);
        return mapper.toDomain(e);
    }
    @Override
    public List<UsuarioOutput> listarTodosPorParametro(String parametroBusca) {
        List<UsuarioEntity> entities = repo.listarTodosPorParametro(parametroBusca);

        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    private UsuarioOutput toResponseDTO(UsuarioEntity entity) {
        return new UsuarioOutput(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getTipo(),
                new UsuarioOutput.EnderecoOutput(
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
        return repo.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existePorId(UUID id) {
        return repo.existsById(id);
    }

    @Override
    public void deletarPorId(UUID id) {
        repo.deleteById(id);
    }
}
