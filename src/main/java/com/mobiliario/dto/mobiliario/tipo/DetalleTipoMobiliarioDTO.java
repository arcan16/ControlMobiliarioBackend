package com.mobiliario.dto.mobiliario.tipo;

import com.mobiliario.models.TipoMobiliarioEntity;

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
