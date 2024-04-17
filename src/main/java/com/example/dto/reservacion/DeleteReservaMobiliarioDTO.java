package com.example.dto.reservacion;

import jakarta.validation.constraints.NotNull;

public record DeleteReservaMobiliarioDTO(@NotNull Long idReservacion,
                                         @NotNull Long idPresentacion) {
}
