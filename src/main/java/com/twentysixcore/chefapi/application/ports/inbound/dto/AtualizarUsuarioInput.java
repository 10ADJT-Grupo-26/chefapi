package com.twentysixcore.chefapi.application.ports.inbound.dto;

public record AtualizarUsuarioInput(
        String nome,
        String email,
        String login,
        String tipo,
        EnderecoInput endereco
) {}