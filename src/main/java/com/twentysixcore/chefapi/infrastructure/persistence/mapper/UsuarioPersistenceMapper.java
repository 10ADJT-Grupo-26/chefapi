package com.twentysixcore.chefapi.infrastructure.persistence.mapper;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.infrastructure.persistence.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UsuarioPersistenceMapper {

    @Mappings({
            @Mapping(target = "tipo", expression = "java(usuario.getTipo().name())"),
            @Mapping(source = "endereco.rua", target = "rua"),
            @Mapping(source = "endereco.numero", target = "numero"),
            @Mapping(source = "endereco.cidade", target = "cidade"),
            @Mapping(source = "endereco.cep", target = "cep"),
            @Mapping(source = "endereco.uf", target = "uf"),
            @Mapping(source = "senha", target = "senha")
    })
    UsuarioEntity toEntity(Usuario usuario);

    @Mappings({
            @Mapping(source = "rua", target = "endereco.rua"),
            @Mapping(source = "numero", target = "endereco.numero"),
            @Mapping(source = "cidade", target = "endereco.cidade"),
            @Mapping(source = "cep", target = "endereco.cep"),
            @Mapping(source = "uf", target = "endereco.uf"),
            @Mapping(target = "tipo", expression = "java(com.twentysixcore.chefapi.application.domain.TipoUsuario.valueOf(entity.getTipo()))"),
            @Mapping(source = "senha", target = "senha")
    })
    Usuario toDomain(UsuarioEntity entity);
}
