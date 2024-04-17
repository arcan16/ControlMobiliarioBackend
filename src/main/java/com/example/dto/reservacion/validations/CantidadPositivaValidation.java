package com.example.dto.reservacion.validations;

import com.example.dto.reservacion.ReservaPrestamoDTO;
import com.example.models.ReservacionEntity;
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
