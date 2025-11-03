package com.twentysixcore.chefapi.application.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nome;
    private String email;
    private String login;
    private String senha;
    private TipoUsuario tipo;
    private Endereco endereco;
    private OffsetDateTime dataUltimaAlteracao;

    public Usuario(String nome, String email, String login, String senha, TipoUsuario tipo, Endereco endereco) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
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

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }
    public void setTipo(String tipo) {
        this.setTipo(TipoUsuario.valueOf(tipo));
    }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public OffsetDateTime getDataUltimaAlteracao() { return dataUltimaAlteracao; }
    public void setDataUltimaAlteracao(OffsetDateTime dataUltimaAlteracao) { this.dataUltimaAlteracao = dataUltimaAlteracao; }

    public void alterarSenha(String novaSenha) {
        if (novaSenha == null) throw new IllegalArgumentException("Nova senha não pode ser nula");
        this.senha = novaSenha;
        this.dataUltimaAlteracao = OffsetDateTime.now();
    }

    public void atualizarDataAlteracao() {
        this.dataUltimaAlteracao = OffsetDateTime.now();
    }
}

