package com.mobiliario.dto.cobro;

import com.mobiliario.dto.reservacion.ReservaPrestamoDTO;
import com.mobiliario.models.ReservacionEntity;

import java.time.LocalDateTime;
import java.util.List;

public record DetalleCobroDTO(Long idReservacion,
                              String cliente,
                              String direccionEntrega,
                              LocalDateTime fechaEntrega,
                              LocalDateTime fechaRecepcion,
                              float total,
                              Integer status,
                              List<ReservaPrestamoDTO> reservPrestamoList) {
    public DetalleCobroDTO(ReservacionEntity reservacion, float total) {
        this(reservacion.getIdReservacion(), reservacion.getCliente().getNombre(), reservacion.getDireccionEntrega(),
                reservacion.getFechaEntrega(), reservacion.getFechaRecepcion(), total, reservacion.getStatus(),
                reservacion.getReservaMobiliarioList().stream().map(ReservaPrestamoDTO::new).toList());
    }


}
