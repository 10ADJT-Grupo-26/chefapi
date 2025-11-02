package com.twentysixcore.chefapi.infrastructure.api.rest.mapper;

import com.twentysixcore.chefapi.application.ports.inbound.dto.*;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UsuarioApiMapper {

    @Mapping(target = "usuarioId", source = "id")
    AlterarSenhaInput toInput(UUID id, AlterarSenhaRequestDTO request);

    AtualizarUsuarioInput toInput(AtualizarUsuarioRequestDTO request);

    CadastrarUsuarioInput toInput(UsuarioRequestDTO generatedRequest);

    LoginInput toInput(LoginRequestDTO request);

    UsuarioResponseDTO toResponse(UsuarioOutput usuario);

    LoginResponseDTO toLoginResponse(UsuarioOutput usuario);
}