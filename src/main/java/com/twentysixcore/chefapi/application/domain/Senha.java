package com.twentysixcore.chefapi.application.domain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class Senha {
    private final String hash;

    public Senha(String hash) {
        this.hash = hash;
    }

    public static Senha criar(String senhaBruta) {
        if (senhaBruta == null || senhaBruta.length() < 6) {
            throw new IllegalArgumentException("Senha inválida: mínimo 6 caracteres");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String h = encoder.encode(senhaBruta);
        return new Senha(h);
    }

    public boolean verificar(String senhaBruta) {
        if (senhaBruta == null) return false;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senhaBruta, this.hash);
    }

    public String getHash() {
        return hash;
    }
}
