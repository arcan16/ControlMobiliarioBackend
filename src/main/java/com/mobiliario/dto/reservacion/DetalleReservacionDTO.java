package com.mobiliario.dto.reservacion;

import com.mobiliario.models.ReservacionEntity;

import java.time.LocalDateTime;
import java.util.List;

public record DetalleReservacionDTO(Long idReservacion,
                                    String cliente,
                                    String direccionEntrega,
                                    LocalDateTime fechaEntrega,
                                    LocalDateTime fechaRecepcion,
                                    Integer status,
                                    List<ReservaPrestamoDTO> reservPrestamoList
                                   ) {
    public DetalleReservacionDTO (ReservacionEntity reservacion) {
        this(reservacion.getIdReservacion(), reservacion.getCliente().getNombre(), reservacion.getDireccionEntrega(),
                reservacion.getFechaEntrega(),reservacion.getFechaRecepcion(),
                reservacion.getStatus(),
                reservacion.getReservaMobiliarioList().stream().map(ReservaPrestamoDTO::new).toList()
                );
    }
}
