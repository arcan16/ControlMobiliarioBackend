package com.example.dto.presentation.validations;

import com.example.dto.presentation.CreatePresentationMobiliarioDTO;
import com.example.models.MobiliarioEntity;

public interface PresentationValidation {

    void validate(MobiliarioEntity mobi, CreatePresentationMobiliarioDTO mobiliario);
}
