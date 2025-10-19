package com.twentysixcore.chefapi.application.mapper;

import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioApplicationMapper {

    @Mapping(target = "tipo", expression = "java(usuario.getTipo().name())")
    UsuarioOutput toOutput(Usuario usuario);
}