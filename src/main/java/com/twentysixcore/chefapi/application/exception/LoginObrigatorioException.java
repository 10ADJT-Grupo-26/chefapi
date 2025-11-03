package com.twentysixcore.chefapi.application.exception;

public class LoginObrigatorioException extends DomainException {
    public LoginObrigatorioException() {
        super("Login é obrigatório.");
    }
}
