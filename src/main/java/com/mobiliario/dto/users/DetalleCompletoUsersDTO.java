package com.mobiliario.dto.users;

import com.mobiliario.models.ERole;
import com.mobiliario.models.UserEntity;

public record DetalleCompletoUsersDTO(Long id,
                                      String username,
                                      String password,
                                      String email,
                                      ERole rol){

    public DetalleCompletoUsersDTO(UserEntity user){
        this(user.getIdUsuario(), user.getUsername(), user.getPassword(), user.getEmail(),user.getRol().getRol());

    }
}
