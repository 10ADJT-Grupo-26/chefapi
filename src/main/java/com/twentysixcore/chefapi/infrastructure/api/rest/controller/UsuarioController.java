package com.twentysixcore.chefapi.infrastructure.api.rest.controller;

import com.twentysixcore.chefapi.application.ports.inbound.AlterarSenha;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.*;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.ApiApi;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.AlterarSenhaRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.AtualizarUsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.UsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.UsuarioResponseDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.mapper.UsuarioApiMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UsuarioController implements ApiApi {

    private final CadastrarUsuario cadastrar;
    private final AtualizarUsuario atualizar;
    private final BuscarUsuarioPorId buscar;
    private final BuscarUsuarioPorNome buscarPorNome;
    private final DeletarUsuarioPorId deletar;
    private final AlterarSenha alterarSenha;
    private final UsuarioApiMapper mapper;

    public UsuarioController(
            CadastrarUsuario cadastrar,
            AtualizarUsuario atualizar,
            BuscarUsuarioPorId buscar,
            BuscarUsuarioPorNome buscarPorNome,
            DeletarUsuarioPorId deletar,
            AlterarSenha alterarSenha,
            UsuarioApiMapper mapper) {
        this.cadastrar = cadastrar;
        this.atualizar = atualizar;
        this.buscar = buscar;
        this.buscarPorNome = buscarPorNome;
        this.deletar = deletar;
        this.alterarSenha = alterarSenha;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(UsuarioRequestDTO request) {
        var usuario = cadastrar.executar(mapper.toInput(request));
        var response = mapper.toResponse(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> atualizar(UUID id, AtualizarUsuarioRequestDTO request) {
        UsuarioOutput usuario = atualizar.executar(id, mapper.toInput(request));
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(usuario));
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(UUID id) {
        var usuario = buscar.executar(id);
        var response = mapper.toResponse(usuario);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> buscarPorNome(String nome) {
        UsuarioOutput usuario = buscarPorNome.executar(nome);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(usuario));
    }

    @Override
    public ResponseEntity<Void> deletarUsuarioPorId(UUID id) {
        deletar.executar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> alterarSenha(AlterarSenhaRequestDTO request) {
        alterarSenha.executar(mapper.toInput(request));
        return ResponseEntity.noContent().build();
    }
}
