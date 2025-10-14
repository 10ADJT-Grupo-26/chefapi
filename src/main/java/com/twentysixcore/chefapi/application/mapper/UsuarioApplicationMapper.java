package com.twentysixcore.chefapi.application.mapper;

import com.twentysixcore.chefapi.application.domain.TipoUsuario;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UsuarioApplicationMapper {

    @Mapping(source = "tipo", target = "tipo", qualifiedByName = "enumToString")
    UsuarioOutput toOutput(Usuario usuario);

    @Named("enumToString")
    default String enumToString(TipoUsuario tipo) {
        return tipo.name();
    }
}