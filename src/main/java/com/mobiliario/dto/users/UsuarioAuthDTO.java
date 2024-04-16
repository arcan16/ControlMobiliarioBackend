package com.mobiliario.dto.users;

import jakarta.validation.constraints.NotBlank;

public record UsuarioAuthDTO (
        @NotBlank String username,
        @NotBlank String password
){
}
