package com.mobiliario.dto.empleados;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroEmployeeDTO(@NotNull @NotBlank String nombre,
                                  @NotNull @NotBlank String apellido,
                                  @NotNull @NotBlank String direccion,
                                  @NotNull @NotBlank String telefono,
                                  @NotNull Long idUsuario) {
}
