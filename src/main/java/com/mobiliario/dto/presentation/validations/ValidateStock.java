package com.mobiliario.dto.presentation.validations;

import com.mobiliario.dto.presentation.CreatePresentationMobiliarioDTO;
import com.mobiliario.models.MobiliarioEntity;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateStock implements PresentationValidation{

    @Override
    public void validate(MobiliarioEntity mobi, CreatePresentationMobiliarioDTO mobiliario) {
        if(mobiliario.cantidad()>mobi.getCantidad()){
            throw new ValidationException("Error. El objeto "+ mobi.getDescripcion()
                    +" tiene una existencia de "+ mobi.getCantidad() +
                    " y estas intentando usar " +  mobiliario.cantidad()+ " unidades");
        }
    }

    public void validate(MobiliarioEntity mobi, MobiliarioEntity mobiliario) {
        if(mobiliario.getCantidad()>mobi.getCantidad()){
            throw new ValidationException("Error. La presentacion"+ mobi.getDescripcion()
                    +" tiene una existencia de "+ mobiliario.getCantidad() +
                    " y estas intentando usar " + mobi.getCantidad() + " unidades");
        }
    }
}
