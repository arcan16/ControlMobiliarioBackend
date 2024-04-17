package com.example.dto.mobiliario;

import com.example.models.MobiliarioEntity;

import java.time.LocalDateTime;

public record DetalleMobiliarioCompletoDTO(Long id,
                                           String tipo,
                                           String descripcion,
                                           Integer cantidad,
                                           LocalDateTime fecha,
                                           String autor) {
    public DetalleMobiliarioCompletoDTO(MobiliarioEntity mobiliario) {
        this(mobiliario.getIdMobiliario(),mobiliario.getTipo().getNombre(), mobiliario.getDescripcion(),
                mobiliario.getCantidad(),mobiliario.getFechaCreacion(),mobiliario.getUsuario().getUsername());
    }

}
