package com.mobiliario.dto.cobro;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TotalPeriodoDTO(@NotNull LocalDateTime fechaInicio,
                              @NotNull LocalDateTime fechaFinal) {
}
