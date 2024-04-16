package com.mobiliario.dto.cobro;

import com.mobiliario.models.CobroEntity;

import java.time.LocalDateTime;

public record GetCobroDTO(Long id,
                          Long idReserva,
                          float total,
                          LocalDateTime fecha,
                          Long idUser,
                          boolean valido) {
    public GetCobroDTO (CobroEntity cobro){
        this(cobro.getIdCobro(),cobro.getReservacion().getIdReservacion(), cobro.getTotal(),
                cobro.getFecha(),cobro.getUsuario().getIdUsuario(), cobro.getValido());
    }
}
