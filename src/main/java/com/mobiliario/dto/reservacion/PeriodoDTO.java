package com.mobiliario.dto.reservacion;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record PeriodoDTO(@NotNull @DateTimeFormat(pattern = "yyyy-MM-dd")
                         @JsonFormat(pattern = "yyyy-MM-dd") Date fecha1,
                         @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd")
                         @JsonFormat(pattern = "yyyy-MM-dd") Date fecha2) {
}


