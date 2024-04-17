package com.example.dto.reservacion;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateReservacionDTO(@NotNull Long idReservacion,
                                   Long idCliente,
                                   String direccionEntrega,
                                   LocalDateTime fechaEntrega,
                                   LocalDateTime fechaRecepcion,
                                   Integer status,
                                   List<ReservaPrestamoDTO> reservPrestamoList
                                   //@Valid List<ReservaPrestamoDTO> reservPrestamoList
                                   ) {
}
