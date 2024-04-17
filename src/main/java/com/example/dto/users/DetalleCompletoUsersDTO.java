package com.example.dto.users;

import com.example.models.ERole;
import com.example.models.UserEntity;

public record DetalleCompletoUsersDTO(Long id,
                                      String username,
                                      String password,
                                      String email,
                                      ERole rol){

    public DetalleCompletoUsersDTO(UserEntity user){
        this(user.getIdUsuario(), user.getUsername(), user.getPassword(), user.getEmail(),user.getRol().getRol());

    }
}
