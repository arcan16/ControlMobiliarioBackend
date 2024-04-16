package com.mobiliario.dto.reservacion.validations;

import com.mobiliario.dto.reservacion.ReservaPrestamoDTO;
import com.mobiliario.models.ReservacionEntity;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CantidadPositivaValidation implements ReservaValidation{

    @Override
    public void Validate(ReservaPrestamoDTO reservaMobiliario, ReservacionEntity reservacion) {
        if(reservaMobiliario.cantidad()<=0){
            throw new ValidationException("Solo se admiten numeros positivos");
        }
    }
}
