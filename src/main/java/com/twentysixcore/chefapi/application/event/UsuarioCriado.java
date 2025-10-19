package com.twentysixcore.chefapi.application.event;
import java.time.OffsetDateTime;
import java.util.UUID;

public class UsuarioCriado {
    private final UUID usuarioId;
    private final String email;
    private final OffsetDateTime dataEvento = OffsetDateTime.now();

    public UsuarioCriado(UUID usuarioId, String email) {
        this.usuarioId = usuarioId;
        this.email = email;
    }

    public UUID getUsuarioId() { return usuarioId; }
    public String getEmail() { return email; }
    public OffsetDateTime getDataEvento() { return dataEvento; }
}
