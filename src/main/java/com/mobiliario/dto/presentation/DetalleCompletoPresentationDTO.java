package com.mobiliario.dto.presentation;

import com.mobiliario.models.PresentationEntity;

public record DetalleCompletoPresentationDTO(Long id,
                                             String description,
                                             Float precio
                                             ){
    public DetalleCompletoPresentationDTO(PresentationEntity presentation) {
        this(presentation.getIdPresentacion(), presentation.getDescripcion(), presentation.getPrecio());
    }
}
