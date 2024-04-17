package com.example.service.users;

import com.example.dto.users.DetalleCompletoUsersDTO;
import com.example.dto.users.DetalleUpdateUserDTO;
import com.example.dto.users.DetalleUpdateUserEmployeeDTO;
import com.example.dto.users.RegistroUsuariosDTO;
import com.example.infra.security.jwt.JwtUtils;
import com.example.models.RolesEntity;
import com.example.models.UserEntity;
import com.example.respositories.RolesRepository;
import com.example.respositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDeleteUpdateValidationService userDeleteUpdateValidationService;

    public UserEntity guardarUsuario(RegistroUsuariosDTO usuario){

        boolean findRol=rolesRepository.existsByRol(usuario.rol());
        RolesEntity rolesEntity;
        if(!findRol){
            rolesEntity=rolesRepository.save(new RolesEntity(usuario.rol()));
//            System.out.println("Rol creado "+rolesEntity.getRol());
        }else{
            rolesEntity=rolesRepository.findByRol(usuario.rol());
//            System.out.println("Rol encontrado "+rolesEntity.getRol());
        }
        UserEntity nuevoUsuario = new UserEntity(usuario,rolesEntity);
        nuevoUsuario.setPassword(passwordEncoder.encode(usuario.password()));
        return userRepository.save(nuevoUsuario);
    }

    private String encriptaPassword(String password){
        return passwordEncoder.encode(password);
    }

    public UserEntity getActualUser(HttpServletRequest request){
        String token = jwtUtils.getTokenFromHeader(request);
        String username = jwtUtils.getUsernameFromToken(token);
        return userRepository.getReferenceByUsername(username);
    }

    public UserEntity update(DetalleCompletoUsersDTO detalleCompletoUsersDTO) {
        UserEntity updateUser = userRepository.getReferenceById(detalleCompletoUsersDTO.id());

        if(detalleCompletoUsersDTO.rol()!=null){
            updateUser.setRol(rolesRepository.findByRol(detalleCompletoUsersDTO.rol()));
        }
        if(detalleCompletoUsersDTO.password()!=null){
            updateUser.setPassword(encriptaPassword(detalleCompletoUsersDTO.password()));
        }
        if(detalleCompletoUsersDTO.username()!=null){
            if(userRepository.existsByUsername(detalleCompletoUsersDTO.username())){
                throw new ValidationException("El usuario ya existe, no es posible utilizarlo");
            }
            updateUser.setUsername(detalleCompletoUsersDTO.username());
        }
        if (detalleCompletoUsersDTO.email()!=null){
            updateUser.setEmail(detalleCompletoUsersDTO.email());
        }

        return new UserEntity();
    }

    public UserEntity updateUser(DetalleUpdateUserDTO updateUserDTO){

        // Buscamos el usuario por medio del id recibido en la peticion
        UserEntity user = userRepository.findById(updateUserDTO.id())
                .orElseThrow(()-> new ValidationException("El id proporcionado no coincide con ningun registro"));

        // Si recibimos username en la peticion validamos su contenido
        if(updateUserDTO.username()!=null){
            // Si el usuario recibido es diferente al usuario almacenado en el registro validamos su disponibilidad
            if(!user.getUsername().equals(updateUserDTO.username())){
                Optional<UserEntity> usuario = userRepository.findByUsername(updateUserDTO.username());
                if(usuario.isPresent())throw new ValidationException("El usuario no esta disponible");
            }
            // Actualizamos el usuario
            user.setUsername(updateUserDTO.username());
        }

        if(updateUserDTO.password()!=null)
            user.setPassword(passwordEncoder.encode(updateUserDTO.password()));

        if(updateUserDTO.email()!=null)
            user.setEmail(updateUserDTO.email());

        if(updateUserDTO.rol()!=null){
            RolesEntity roles = rolesRepository.findByRol(updateUserDTO.rol());
            if(roles==null){
                RolesEntity rol = rolesRepository.save(new RolesEntity(updateUserDTO.rol()));
                user.setRol(rol);
            }else{
                user.setRol(roles);
            }
        }

        userRepository.save(user);
        return user;
    }

    public UserEntity updateUser(DetalleUpdateUserEmployeeDTO detalleUpdateUserDTO) {
        // Buscamos el usuario por medio del id recibido en la peticion
        UserEntity user = userRepository.findById(detalleUpdateUserDTO.id())
                .orElseThrow(()-> new ValidationException("El id proporcionado no coincide con ningun registro"));

        // Si recibimos username en la peticion validamos su contenido
        if(detalleUpdateUserDTO.username()!=null){
            // Si el usuario recibido es diferente al usuario almacenado en el registro validamos su disponibilidad
            if(!user.getUsername().equals(detalleUpdateUserDTO.username())){
                Optional<UserEntity> usuario = userRepository.findByUsername(detalleUpdateUserDTO.username());
                if(usuario.isPresent())throw new ValidationException("El usuario no esta disponible");
            }
            // Actualizamos el usuario
            user.setUsername(detalleUpdateUserDTO.username());
        }

        if(detalleUpdateUserDTO.password()!=null)
            user.setPassword(passwordEncoder.encode(detalleUpdateUserDTO.password()));

        if(detalleUpdateUserDTO.email()!=null)
            user.setEmail(detalleUpdateUserDTO.email());

        if(detalleUpdateUserDTO.rol()!=null){
            RolesEntity roles = rolesRepository.findByRol(detalleUpdateUserDTO.rol());
            if(roles==null){
                RolesEntity rol = rolesRepository.save(new RolesEntity(detalleUpdateUserDTO.rol()));
                user.setRol(rol);
            }else{
                user.setRol(roles);
            }
        }

        return user;
    }
}
