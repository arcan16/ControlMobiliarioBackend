package com.mobiliario.dto.reservacion.validations;

import com.mobiliario.dto.reservacion.ReservaPrestamoDTO;
import com.mobiliario.models.ReservacionEntity;

public interface ReservaValidation {

    public void Validate(ReservaPrestamoDTO reservaMobiliario,
                         ReservacionEntity reservacion);
}
