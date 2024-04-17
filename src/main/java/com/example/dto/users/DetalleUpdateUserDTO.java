package com.example.dto.users;

import com.example.models.ERole;
import jakarta.validation.constraints.NotNull;

public record DetalleUpdateUserDTO(@NotNull Long id,
                                   String username,
                                   String password,
                                   String email,
                                   ERole rol) {
}
