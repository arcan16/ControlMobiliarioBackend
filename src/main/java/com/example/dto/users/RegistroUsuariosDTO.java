package com.example.dto.users;

import com.example.models.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroUsuariosDTO(@NotBlank String username,
                                  @NotBlank String password,
                                  @NotBlank @Email String email,
                                  @NotNull ERole rol) {
}
