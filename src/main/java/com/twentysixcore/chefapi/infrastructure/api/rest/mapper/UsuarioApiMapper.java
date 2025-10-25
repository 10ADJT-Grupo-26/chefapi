package com.twentysixcore.chefapi.infrastructure.api.rest.mapper;

import com.twentysixcore.chefapi.application.ports.inbound.dto.AtualizarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.CadastrarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.AtualizarUsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioApiMapper {
    CadastrarUsuarioInput toInput(UsuarioRequestDTO request);

    AtualizarUsuarioInput toInput(AtualizarUsuarioRequestDTO request);

    UsuarioResponseDTO toResponse(UsuarioOutput output);
}