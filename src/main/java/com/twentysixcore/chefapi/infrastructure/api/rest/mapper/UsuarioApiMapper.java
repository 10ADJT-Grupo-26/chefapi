package com.twentysixcore.chefapi.infrastructure.api.rest.mapper;

import com.twentysixcore.chefapi.application.ports.inbound.dto.AlterarSenhaInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.AtualizarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.CadastrarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.AlterarSenhaRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.AtualizarUsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.UsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.UsuarioResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioApiMapper {

    AlterarSenhaInput toInput(AlterarSenhaRequestDTO request);

    AtualizarUsuarioInput toInput(AtualizarUsuarioRequestDTO request);

    CadastrarUsuarioInput toInput(UsuarioRequestDTO generatedRequest);

    UsuarioResponseDTO toResponse(UsuarioOutput usuario);
}