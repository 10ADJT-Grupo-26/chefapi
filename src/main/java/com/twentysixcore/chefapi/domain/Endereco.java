package com.twentysixcore.chefapi.domain;

public class Endereco {
    private String rua;
    private String numero;
    private String cidade;
    private String cep;
    private String uf;

    public Endereco() {}

    public Endereco(String rua, String numero, String cidade, String cep, String uf) {
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.cep = cep;
        this.uf = uf;
    }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
}
