package com.mobiliario.dto.users;

import com.mobiliario.models.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroUsuariosDTO(@NotBlank String username,
                                  @NotBlank String password,
                                  @NotBlank @Email String email,
                                  @NotNull ERole rol) {
}
