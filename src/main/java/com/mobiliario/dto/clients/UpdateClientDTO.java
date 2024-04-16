package com.mobiliario.dto.clients;

import jakarta.validation.constraints.NotNull;

public record UpdateClientDTO(@NotNull Long id,
                              String nombre,
                              String direccion,
                              String telefono) {
}
