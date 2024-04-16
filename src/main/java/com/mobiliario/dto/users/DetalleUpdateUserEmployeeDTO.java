package com.mobiliario.dto.users;

import com.mobiliario.models.ERole;
import jakarta.validation.constraints.NotNull;

public record DetalleUpdateUserEmployeeDTO(@NotNull Long id,
                                           String username,
                                           String password,
                                           String email,
                                           ERole rol,
                                           String nombre,
                                           String apellido,
                                           String direccion,
                                           String telefono) {
}
