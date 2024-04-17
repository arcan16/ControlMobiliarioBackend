package com.example.component;

import com.example.models.ERole;
import com.example.models.RolesEntity;
import com.example.models.UserEntity;
import com.example.respositories.RolesRepository;
import com.example.respositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
/*
* Esta clase creara un usuario administrador en la primer ejecucion del programa y/o en caso de
* que no exista ningun usuario de tipo ADMIN
* */
@Component
public class DatabaseInitializerComponent {
    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${myapp.userinit}")
    private String userInit;

    @Value("${myapp.passwordinit}")
    private String passwordInit;


    @PostConstruct
    public void init(){
        boolean existeRolAdmin = rolesRepository.existsByRol(ERole.ADMIN);

        if(!existeRolAdmin){
            var rol = rolesRepository.save(new RolesEntity(ERole.ADMIN));
            UserEntity userEntity = new UserEntity(userInit, passwordEncoder.encode(passwordInit), rol);
            userRepository.save(userEntity);
        }else {
            var rol = rolesRepository.findByRol(ERole.ADMIN);
            if(!userRepository.existsByRol(rol)){
                userRepository.save(new UserEntity(userInit, passwordEncoder.encode(passwordInit), rol));
            }
        }
    }
}
