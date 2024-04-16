package com.mobiliario.dto.mobiliario.tipo;

import jakarta.validation.constraints.NotNull;

public record UpdateTipoMobiliario(@NotNull Long id,
                                   String nombre) {
}
