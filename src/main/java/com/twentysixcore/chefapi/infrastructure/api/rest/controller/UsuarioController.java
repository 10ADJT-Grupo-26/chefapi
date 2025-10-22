package com.twentysixcore.chefapi.infrastructure.api.rest.controller;

import com.twentysixcore.chefapi.application.ports.inbound.AlterarSenha;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.*;
import com.twentysixcore.chefapi.application.usecase.BuscarUsuarioPorNomeUseCase;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.AlterarSenhaRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.AtualizarUsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.ApiApi;
import com.twentysixcore.chefapi.infrastructure.api.rest.mapper.UsuarioApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            BuscarUsuarioPorNomeUseCase buscarPorNome,
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
        var usuario = cadastrar.executar(mapper.toInputFromGenerated(request));
        var response = mapper.toGeneratedResponse(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable("id") UUID id, @Valid @RequestBody AtualizarUsuarioRequestDTO request) {
        UsuarioOutput usuario = atualizar.executar(id, mapper.toInput(request));
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(usuario));
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(UUID id) {
        var usuario = buscar.executar(id);
        var response = mapper.toGeneratedResponse(usuario); // converte para generated.model
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<UsuarioResponseDTO> buscarPorNome(@RequestParam("nome") String nome) {
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
