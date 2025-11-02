package com.twentysixcore.chefapi.infrastructure.persistence;

import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioJPARepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByEmail(String email);

    @Query("SELECT u FROM UsuarioEntity u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Optional<UsuarioEntity> findByNome(@Param("nome") String nome);
    Optional<UsuarioEntity> findById(@NonNull UUID id);
    void deleteById(@NonNull UUID id);
    void deleteAll();
    boolean existsById(@NonNull UUID id);
    Optional<UsuarioEntity> findByLogin(String login);
}
