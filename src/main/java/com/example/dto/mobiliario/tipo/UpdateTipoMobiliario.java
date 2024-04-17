package com.example.dto.mobiliario.tipo;

import jakarta.validation.constraints.NotNull;

public record UpdateTipoMobiliario(@NotNull Long id,
                                   String nombre) {
}
