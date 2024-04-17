package com.example.dto.reservacion;

import com.example.models.ReservaMobiliarioEntity;
import jakarta.validation.constraints.NotNull;

public record ReservaPrestamoDTO(@NotNull Long idPresentacion,
                                 @NotNull Integer cantidad,
                                 @NotNull String presentacion) {

    public ReservaPrestamoDTO(ReservaMobiliarioEntity reservacion) {
        this(reservacion.getId().getIdPresentacion(), reservacion.getCantidad(), reservacion.getPresentation().getDescripcion());
    }
}
