package com.twentysixcore.chefapi.application.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nome;
    private String email;
    private String login;
    private String passwordHash;
    private TipoUsuario tipo;
    private Endereco endereco;
    private OffsetDateTime dataUltimaAlteracao;

    public Usuario(String nome, String email, String login, Senha senha, TipoUsuario tipo, Endereco endereco) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.passwordHash = senha != null ? senha.getHash() : null;
        this.tipo = tipo;
        this.endereco = endereco;
        this.dataUltimaAlteracao = OffsetDateTime.now();
    }

    public Usuario() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public OffsetDateTime getDataUltimaAlteracao() { return dataUltimaAlteracao; }
    public void setDataUltimaAlteracao(OffsetDateTime dataUltimaAlteracao) { this.dataUltimaAlteracao = dataUltimaAlteracao; }

    public void alterarSenha(Senha novaSenha) {
        if (novaSenha == null) throw new IllegalArgumentException("Nova senha não pode ser nula");
        this.passwordHash = novaSenha.getHash();
        this.dataUltimaAlteracao = OffsetDateTime.now();
    }

    public void atualizarDados(String nome, Endereco endereco) {
        if (nome != null && !nome.isBlank()) this.nome = nome;
        this.endereco = endereco;
        this.dataUltimaAlteracao = OffsetDateTime.now();
    }
}

