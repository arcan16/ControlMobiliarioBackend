package com.example.dto.presentation;

import com.example.models.PresentationMobiliarioEntity;
import jakarta.validation.constraints.NotNull;

public record CreatePresentationMobiliarioDTO(@NotNull Long idMobiliario,
                                              @NotNull Integer cantidad) {
    public CreatePresentationMobiliarioDTO(PresentationMobiliarioEntity presentationMobiliario) {
        this(presentationMobiliario.getMobiliario().getIdMobiliario(), presentationMobiliario.getCantidad());
    }
}
