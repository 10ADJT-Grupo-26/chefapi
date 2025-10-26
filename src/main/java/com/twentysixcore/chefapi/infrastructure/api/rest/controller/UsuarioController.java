package com.twentysixcore.chefapi.infrastructure.api.rest.controller;

import com.twentysixcore.chefapi.application.ports.inbound.AlterarSenha;
import com.twentysixcore.chefapi.application.ports.inbound.dto.AlterarSenhaInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.AtualizarUsuario;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.BuscarUsuarioPorId;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.DeletarUsuarioPorId;
import com.twentysixcore.chefapi.application.usecase.BuscarUsuarioPorIdUseCase;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.AlterarSenhaRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.AtualizarUsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.CadastrarUsuario;
import com.twentysixcore.chefapi.application.usecase.CadastrarUsuarioUseCase;
import com.twentysixcore.chefapi.infrastructure.api.rest.mapper.UsuarioApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final CadastrarUsuario cadastrar;
    private final AtualizarUsuario atualizar;
    private final BuscarUsuarioPorId buscar;
    private final DeletarUsuarioPorId deletar;
    private final AlterarSenha alterarSenha;
    private final UsuarioApiMapper mapper;

    public UsuarioController(CadastrarUsuarioUseCase cadastrar, BuscarUsuarioPorIdUseCase buscar, DeletarUsuarioPorId deletar, AlterarSenha alterarSenha, UsuarioApiMapper mapper) {
    public UsuarioController(
            CadastrarUsuarioUseCase cadastrar,
            AtualizarUsuario atualizar,
            BuscarUsuarioPorIdUseCase buscar,
            DeletarUsuarioPorId deletar,
            UsuarioApiMapper mapper) {
        this.cadastrar = cadastrar;
        this.atualizar = atualizar;
        this.buscar = buscar;
        this.deletar = deletar;
        this.alterarSenha = alterarSenha;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioOutput usuario = cadastrar.executar(mapper.toInput(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable("id") UUID id, @Valid @RequestBody AtualizarUsuarioRequestDTO request) {
        UsuarioOutput usuario = atualizar.executar(id, mapper.toInput(request));
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable("id") UUID id) {
        UsuarioOutput usuario = buscar.executar(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") UUID id) {
        deletar.executar(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody AlterarSenhaRequestDTO request) {
        alterarSenha.executar(mapper.toInput(request));
        return ResponseEntity.noContent().build();
    }
}
