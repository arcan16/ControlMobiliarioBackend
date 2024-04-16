package com.mobiliario.dto.mobiliario;

import jakarta.validation.constraints.NotNull;

public record CreateMobiliarioDTO(@NotNull Long tipo,
                                  @NotNull String descripcion,
                                  @NotNull Integer cantidad
                                  ) {
}
