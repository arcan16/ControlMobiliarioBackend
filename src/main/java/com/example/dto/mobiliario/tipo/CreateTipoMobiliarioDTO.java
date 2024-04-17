package com.example.dto.mobiliario.tipo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTipoMobiliarioDTO(@NotNull @NotBlank String nombre) {
}
