package com.example.dto.mobiliario;

import jakarta.validation.constraints.NotNull;

public record UpdateMobiliarioDTO(@NotNull Long id,
                                  String tipo,
                                  String descripcion,
                                  Integer cantidad) {
}
