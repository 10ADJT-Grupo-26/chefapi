package com.twentysixcore.chefapi.infrastructure.rest.controller;

import com.twentysixcore.chefapi.application.dto.UsuarioCriadoDTO;
import com.twentysixcore.chefapi.application.dto.UsuarioResponseDTO;
import com.twentysixcore.chefapi.application.usecase.CadastrarUsuarioUseCase;
import com.twentysixcore.chefapi.application.usecase.ProcurarUsuarioUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrar;
    private final ProcurarUsuarioUseCase procurar;

    public UsuarioController(CadastrarUsuarioUseCase cadastrar, ProcurarUsuarioUseCase procurar) { this.cadastrar = cadastrar;
        this.procurar = procurar;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioCriadoDTO dto) {
        UsuarioResponseDTO resp = cadastrar.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
    @GetMapping("/procurar")
    public ResponseEntity<List<UsuarioResponseDTO>> listar(@RequestParam(required = false) String parametrobusca) {
    List<UsuarioResponseDTO> listaUsuarios = procurar.executar(parametrobusca).getBody();
        return ResponseEntity.status(HttpStatus.OK).body(listaUsuarios);
    }
}
