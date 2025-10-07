package com.twentysixcore.chefapi.application.dto;

import com.twentysixcore.chefapi.domain.Endereco;
import com.twentysixcore.chefapi.domain.TipoUsuario;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(
                                 UUID id,
                                 String nome,
                                 String email,
                                 String login,
                                 TipoUsuario tipo,
                                 Endereco endereco,
                                 OffsetDateTime dataUltimaAlteracao
) {}
