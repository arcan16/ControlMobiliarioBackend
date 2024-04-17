package com.example.dto.presentation;

import com.example.models.PresentationEntity;

import java.util.Date;
import java.util.List;

public record PresentacionDTO(Long id,
                              String descripcion,
                              Float precio,
                              Date fecha,
                              List<PresentacionMobiliarioDTO> presentacionMobiliarioDTOList) {
    public PresentacionDTO (PresentationEntity presentacion){
        this(presentacion.getIdPresentacion(), presentacion.getDescripcion(), presentacion.getPrecio(),
                presentacion.getFechaCreacion(),
                presentacion.getPresentationMobiliarioEntityList().stream().map(PresentacionMobiliarioDTO::new).toList());
    }
}
