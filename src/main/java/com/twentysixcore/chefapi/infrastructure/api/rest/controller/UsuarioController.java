package com.twentysixcore.chefapi.infrastructure.api.rest.controller;

import com.twentysixcore.chefapi.application.ports.inbound.usecase.BuscarUsuarioPorId;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.DeletarUsuarioPorId;
import com.twentysixcore.chefapi.application.usecase.BuscarUsuarioPorIdUseCase;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioRequestDTO;
import com.twentysixcore.chefapi.infrastructure.api.rest.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.CadastrarUsuario;
import com.twentysixcore.chefapi.application.usecase.CadastrarUsuarioUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final CadastrarUsuario cadastrar;
    private final BuscarUsuarioPorId buscar;
    private final DeletarUsuarioPorId deletar;

    public UsuarioController(CadastrarUsuarioUseCase cadastrar, BuscarUsuarioPorIdUseCase buscar, DeletarUsuarioPorId deletar) {
        this.cadastrar = cadastrar;
        this.buscar = buscar;
        this.deletar = deletar;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO resp = cadastrar.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable("id") UUID id) {
        UsuarioResponseDTO response = buscar.executar(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") UUID id) {
        deletar.executar(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
