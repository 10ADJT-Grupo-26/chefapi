package com.twentysixcore.chefapi.application.exception;

public class EmailObrigatorioException extends DomainException {
    public EmailObrigatorioException() {
        super("E-mail é obrigatório.");
    }
}
