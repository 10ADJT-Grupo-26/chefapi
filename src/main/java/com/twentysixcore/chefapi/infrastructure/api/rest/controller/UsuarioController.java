package com.twentysixcore.chefapi.infrastructure.api.rest.controller;

import com.twentysixcore.chefapi.application.ports.inbound.AlterarSenha;
import com.twentysixcore.chefapi.application.ports.inbound.dto.AlterarSenhaInput;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.BuscarUsuarioPorId;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.DeletarUsuarioPorId;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.CadastrarUsuario;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.ApiApi;
import com.twentysixcore.chefapi.infrastructure.api.rest.generated.model.AlterarSenhaRequestDTO;
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
    private final BuscarUsuarioPorId buscar;
    private final DeletarUsuarioPorId deletar;
    private final AlterarSenha alterarSenha;
    private final UsuarioApiMapper mapper;

    public UsuarioController(
            CadastrarUsuario cadastrar,
            BuscarUsuarioPorId buscar,
            DeletarUsuarioPorId deletar,
            AlterarSenha alterarSenha,
            UsuarioApiMapper mapper) {
        this.cadastrar = cadastrar;
        this.buscar = buscar;
        this.deletar = deletar;
        this.alterarSenha = alterarSenha;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Void> alterarSenha(AlterarSenhaRequestDTO request) {
        alterarSenha.executar(new AlterarSenhaInput(
                request.getUsuarioId(),
                request.getSenhaAtual(),
                request.getNovaSenha()
        ));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(UUID id) {
        var usuario = buscar.executar(id);
        var response = mapper.toGeneratedResponse(usuario); // converte para generated.model
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(UsuarioRequestDTO request) {
        var usuario = cadastrar.executar(mapper.toInputFromGenerated(request));
        var response = mapper.toGeneratedResponse(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Void> deletarUsuarioPorId(UUID id) {
        deletar.executar(id);
        return ResponseEntity.noContent().build();
    }
}
