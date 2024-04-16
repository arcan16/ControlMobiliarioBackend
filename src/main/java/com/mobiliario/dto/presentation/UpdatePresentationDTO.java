package com.mobiliario.dto.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record UpdatePresentationDTO (@NotNull Long id,
                                     String descripcion,
                                     Float precio,
                                     Date fecha,
                                     @Valid List<CreatePresentationMobiliarioDTO> updatePresentationMobiliarioDTOList
                                     ){

}
