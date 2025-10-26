package com.twentysixcore.chefapi.application.ports.inbound.dto;

public record CadastrarUsuarioInput(
        String nome,
        String email,
        String login,
        String senha,
        String tipo,
        EnderecoInput endereco
) {
}