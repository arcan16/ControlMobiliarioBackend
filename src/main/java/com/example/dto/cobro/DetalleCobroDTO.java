package com.example.dto.cobro;

import com.example.dto.reservacion.ReservaPrestamoDTO;
import com.example.models.ReservacionEntity;

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
