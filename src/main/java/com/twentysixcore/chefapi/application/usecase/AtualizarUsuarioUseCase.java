package com.twentysixcore.chefapi.application.usecase;

import com.twentysixcore.chefapi.application.domain.Endereco;
import com.twentysixcore.chefapi.application.domain.Usuario;
import com.twentysixcore.chefapi.application.exception.EmailJaCadastradoException;
import com.twentysixcore.chefapi.application.mapper.UsuarioApplicationMapper;
import com.twentysixcore.chefapi.application.ports.inbound.dto.AtualizarUsuarioInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.EnderecoInput;
import com.twentysixcore.chefapi.application.ports.inbound.dto.UsuarioOutput;
import com.twentysixcore.chefapi.application.ports.inbound.usecase.AtualizarUsuario;
import com.twentysixcore.chefapi.application.ports.outbound.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AtualizarUsuarioUseCase implements AtualizarUsuario {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioApplicationMapper mapper;

    public AtualizarUsuarioUseCase(UsuarioRepository usuarioRepository, UsuarioApplicationMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public UsuarioOutput executar(UUID id, AtualizarUsuarioInput dto) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Nenhum usuário encontrado com o id: " + id));

        Optional.ofNullable(dto.nome()).ifPresent(usuario::setNome);
        Optional.ofNullable(dto.email()).ifPresent(email -> {
            validaEmailExistente(email, usuario);
            usuario.setEmail(email);
        });
        Optional.ofNullable(dto.login()).ifPresent(usuario::setLogin);
        Optional.ofNullable(dto.tipo()).ifPresent(usuario::setTipo);
        Optional.ofNullable(dto.endereco()).ifPresent(enderecoInput -> atualizaEndereco(enderecoInput, usuario.getEndereco()));
        usuario.atualizarDataAlteracao();

        usuarioRepository.salvar(usuario);
        return mapper.toOutput(usuario);
    }

    private static void atualizaEndereco(EnderecoInput enderecoInput, Endereco endereco) {
        Optional.ofNullable(enderecoInput.rua()).ifPresent(endereco::setRua);
        Optional.ofNullable(enderecoInput.numero()).ifPresent(endereco::setNumero);
        Optional.ofNullable(enderecoInput.cidade()).ifPresent(endereco::setCidade);
        Optional.ofNullable(enderecoInput.cep()).ifPresent(endereco::setCep);
        Optional.ofNullable(enderecoInput.uf()).ifPresent(endereco::setUf);
    }

    private void validaEmailExistente(String email, Usuario usuario) {
        if (usuarioRepository.buscarPorEmail(email).isPresent() && !usuario.getEmail().equals(email))
            throw new EmailJaCadastradoException(email);
    }
}
