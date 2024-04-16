package com.mobiliario.dto.presentation.validations;

import com.mobiliario.dto.presentation.CreatePresentationMobiliarioDTO;
import com.mobiliario.models.MobiliarioEntity;

public interface PresentationValidation {

    void validate(MobiliarioEntity mobi, CreatePresentationMobiliarioDTO mobiliario);
}
