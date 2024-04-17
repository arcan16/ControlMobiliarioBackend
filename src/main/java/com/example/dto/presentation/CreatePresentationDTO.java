package com.example.dto.presentation;

import com.example.models.PresentationEntity;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreatePresentationDTO(@NotNull String description,
                                    @NotNull Float precio,
                                    @NotNull List<CreatePresentationMobiliarioDTO> presentationMobiliarioList){

    public CreatePresentationDTO(PresentationEntity presentationToUpdate) {
        this(presentationToUpdate.getDescripcion(), presentationToUpdate.getPrecio(), null);
    }
}
