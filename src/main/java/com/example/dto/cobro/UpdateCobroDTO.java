package com.example.dto.cobro;

import com.example.models.CobroEntity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record UpdateCobroDTO(@NotNull Long idCobro,
                             @DecimalMin(value = "0.1", message = "El valor de 'total' debe ser mayor que cero") float total,
                             Boolean valido) {
    public UpdateCobroDTO (CobroEntity cobro){
        this(cobro.getIdCobro(), cobro.getTotal(), cobro.getValido());
    }
}
