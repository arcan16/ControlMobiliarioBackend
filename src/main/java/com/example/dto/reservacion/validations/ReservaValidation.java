package com.example.dto.reservacion.validations;

import com.example.dto.reservacion.ReservaPrestamoDTO;
import com.example.models.ReservacionEntity;

public interface ReservaValidation {

    public void Validate(ReservaPrestamoDTO reservaMobiliario,
                         ReservacionEntity reservacion);
}
