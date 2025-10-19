package com.twentysixcore.chefapi.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioJPARepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByEmail(String email);

    @Query("""
    SELECT  u
    FROM UsuarioEntity u
    WHERE LOWER(u.nome) LIKE '%' || LOWER(:parametroBusca) || '%'
       OR LOWER(u.email) LIKE '%' || LOWER(:parametroBusca) || '%'
       OR LOWER(u.login) LIKE '%' || LOWER(:parametroBusca) || '%'
""")
    List<UsuarioEntity> listarTodosPorParametro(@Param("parametroBusca") String parametroBusca);
}
