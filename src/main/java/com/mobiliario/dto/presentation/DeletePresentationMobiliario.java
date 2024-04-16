package com.mobiliario.dto.presentation;

import jakarta.validation.constraints.NotNull;

public record DeletePresentationMobiliario(@NotNull Long idPresentation,
                                           @NotNull Long idMobiliario) {
}
