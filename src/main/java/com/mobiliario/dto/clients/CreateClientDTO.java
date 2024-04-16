package com.mobiliario.dto.clients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateClientDTO(@NotNull @NotBlank String nombre,
                              @NotNull @NotBlank String direccion,
                              @NotNull @NotBlank String telefono) {
}
