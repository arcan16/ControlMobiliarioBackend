package com.mobiliario.dto.users;

import com.mobiliario.models.ERole;
import com.mobiliario.models.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DetalleUsuarioDTO (@NotBlank Long id,
                                 @NotBlank String username,
                                 @NotBlank String password,
                                 @NotBlank @Email String email,
                                 @NotNull ERole rol){
    public DetalleUsuarioDTO(UserEntity userEntity) {
        this(userEntity.getIdUsuario(),userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getRol().getRol());
    }
}
