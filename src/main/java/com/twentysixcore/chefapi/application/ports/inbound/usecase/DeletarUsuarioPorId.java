package com.twentysixcore.chefapi.application.ports.inbound.usecase;

import java.util.UUID;

public interface DeletarUsuarioPorId {
    void executar(UUID id);
}