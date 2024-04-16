package com.mobiliario.dto.presentation;

import com.mobiliario.models.PresentationMobiliarioEntity;

public record PresentacionMobiliarioDTO(Long idMobiliario,
                                        String Mobiliario,
                                        Integer cantidad) {
    public PresentacionMobiliarioDTO (PresentationMobiliarioEntity presentacionMobiliario){
        this(presentacionMobiliario.getMobiliario().getIdMobiliario(),
                presentacionMobiliario.getMobiliario().getDescripcion(), presentacionMobiliario.getCantidad());
    }
}
