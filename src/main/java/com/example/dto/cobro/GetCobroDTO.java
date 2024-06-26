package com.example.dto.cobro;

import com.example.models.CobroEntity;

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
