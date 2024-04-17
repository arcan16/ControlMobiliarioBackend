package com.example.dto.presentation;

import com.example.models.PresentationEntity;

public record DetalleCompletoPresentationDTO(Long id,
                                             String description,
                                             Float precio
                                             ){
    public DetalleCompletoPresentationDTO(PresentationEntity presentation) {
        this(presentation.getIdPresentacion(), presentation.getDescripcion(), presentation.getPrecio());
    }
}
