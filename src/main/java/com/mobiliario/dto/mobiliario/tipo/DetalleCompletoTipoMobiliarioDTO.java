package com.mobiliario.dto.mobiliario.tipo;

import com.mobiliario.models.TipoMobiliarioEntity;
import com.mobiliario.models.UserEntity;

import java.time.LocalDateTime;

public record DetalleCompletoTipoMobiliarioDTO (Long id,
                                                String nombre,
                                                LocalDateTime fecha,
                                                UserEntity usuario){
    public DetalleCompletoTipoMobiliarioDTO(TipoMobiliarioEntity mobiliario) {
        this(mobiliario.getIdTipo(), mobiliario.getNombre(), mobiliario.getFechaCreacion(),
                mobiliario.getUsuario());
    }
}
