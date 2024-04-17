package com.example.dto.mobiliario.tipo;

import com.example.models.TipoMobiliarioEntity;

import java.time.LocalDateTime;

public record DetalleTipoMobiliarioDTO(Long id,
                                       String nombre,
                                       LocalDateTime fecha_creacion,
                                       String autor) {
    public DetalleTipoMobiliarioDTO(TipoMobiliarioEntity tipoMobiliario) {
        this(tipoMobiliario.getIdTipo(), tipoMobiliario.getNombre(), tipoMobiliario.getFechaCreacion(),
                tipoMobiliario.getUsuario().getUsername());
    }
}
