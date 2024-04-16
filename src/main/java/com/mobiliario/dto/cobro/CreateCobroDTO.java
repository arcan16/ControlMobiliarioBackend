package com.mobiliario.dto.cobro;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateCobroDTO(@NotNull Long idReservacion,
                             @DecimalMin(value = "0.1", message = "El valor de 'total' debe ser mayor que cero") float total) {
}
