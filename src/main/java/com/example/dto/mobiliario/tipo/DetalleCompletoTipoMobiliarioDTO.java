package com.example.dto.mobiliario.tipo;

import com.example.models.TipoMobiliarioEntity;
import com.example.models.UserEntity;

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
