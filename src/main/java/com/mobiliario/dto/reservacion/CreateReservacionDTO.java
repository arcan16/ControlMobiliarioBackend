package com.mobiliario.dto.reservacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateReservacionDTO(@NotNull Long idCliente,
                                   @NotNull @NotBlank String direccionEntrega,
                                   @NotNull LocalDateTime fechaEntrega,
                                   @NotNull LocalDateTime fechaRecepcion,
                                   @NotNull List<ReservaPrestamoDTO> reservPrestamoList
                                   ) {
}
