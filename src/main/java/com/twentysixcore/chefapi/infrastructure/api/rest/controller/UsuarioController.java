package com.twentysixcore.chefapi.infrastructure.api.rest.controller;

import com.twentysixcore.chefapi.application.ports.inbound.AlterarSenha;
import com.twentysixcore.chefapi.application.ports.inbound.ValidarLogin;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.*;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.ApiApi;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.*;
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
    private final ValidarLogin validarLogin;
    private final UsuarioApiMapper mapper;

    public UsuarioController(
            CadastrarUsuario cadastrar,
            AtualizarUsuario atualizar,
            BuscarUsuarioPorId buscar,
            BuscarUsuarioPorNome buscarPorNome,
            DeletarUsuarioPorId deletar,
            AlterarSenha alterarSenha, ValidarLogin validarLogin,
            UsuarioApiMapper mapper) {
        this.cadastrar = cadastrar;
        this.atualizar = atualizar;
        this.buscar = buscar;
        this.buscarPorNome = buscarPorNome;
        this.deletar = deletar;
        this.alterarSenha = alterarSenha;
        this.validarLogin = validarLogin;
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
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            var usuario = validarLogin.executar(mapper.toInput(request));
            var response = mapper.toLoginResponse(usuario);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Override
    public ResponseEntity<Void> alterarSenha(@PathVariable("id") UUID id,
                                             @RequestBody AlterarSenhaRequestDTO request) {
        alterarSenha.executar(mapper.toInput(id, request));
        return ResponseEntity.noContent().build();
    }
}
