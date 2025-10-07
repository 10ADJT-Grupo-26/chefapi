package com.twentysixcore.chefapi.infrastructure.rest.controller;

import com.twentysixcore.chefapi.application.dto.UsuarioCriadoDTO;
import com.twentysixcore.chefapi.application.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.application.usecase.CadastrarUsuarioUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrar;

    public UsuarioController(CadastrarUsuarioUseCase cadastrar) { this.cadastrar = cadastrar; }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioCriadoDTO dto) {
        UsuarioResponseDTO resp = cadastrar.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
