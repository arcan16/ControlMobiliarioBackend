package com.mobiliario.service.users;

import com.mobiliario.dto.users.DetalleCompletoUsersDTO;
import com.mobiliario.dto.users.DetalleUpdateUserDTO;
import com.mobiliario.dto.users.DetalleUpdateUserEmployeeDTO;
import com.mobiliario.infra.security.jwt.JwtUtils;
import com.mobiliario.models.UserEntity;
import com.mobiliario.respositories.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDeleteUpdateValidationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    public void isAutoDelete(String thisUsername, Long idUserToDelete){
        if(userRepository.existsById(idUserToDelete)){
            UserEntity userToDelete = userRepository.getReferenceById(idUserToDelete);
            if(userToDelete.getUsername().equals(thisUsername)){
                throw new ValidationException("No es posible eliminarse a si mismo");
            }
        }
    }

    public void isAutoUpdate(DetalleCompletoUsersDTO dataToUpdate, String tokenUserActual){
            UserEntity userToUpdate = userRepository.findById(dataToUpdate.id())
                .orElseThrow(()->new ValidationException("El id proporcionado no coincide con ningun registro"));

            UserEntity userActualEntity = userRepository.
                    getReferenceByUsername(jwtUtils.getUsernameFromToken(tokenUserActual));

            // Verificamos si los datos para edicion corresponden con el usuario actual
            if(userActualEntity.getIdUsuario().equals(dataToUpdate.id())){
                // Verificamos si recibimos rol
                if(dataToUpdate.rol()!=null){
                    // Verificamos si el rol recibido es diferente al que tenemos actualmente
                    if(!userActualEntity.getRol().equals(dataToUpdate.rol())){
                        throw new ValidationException("No es posible editar su propio rol");
                    }
                }
            }
    }

    public void isAutoUpdate(DetalleUpdateUserDTO detalleUpdateUserDTO, String tokenUserActual) {
        UserEntity userToUpdate = userRepository.findById(detalleUpdateUserDTO.id())
                .orElseThrow(()->new ValidationException("El id proporcionado no coincide con ningun registro"));

        UserEntity userActualEntity = userRepository.
                getReferenceByUsername(jwtUtils.getUsernameFromToken(tokenUserActual));

        // Verificamos si los datos para edicion corresponden con el usuario actual
        if(userActualEntity.getIdUsuario().equals(detalleUpdateUserDTO.id())){
            // Verificamos si recibimos rol
            if(detalleUpdateUserDTO.rol()!=null){
                // Verificamos si el rol recibido es diferente al que tenemos actualmente
                if(!userActualEntity.getRol().equals(detalleUpdateUserDTO.rol())){
                    throw new ValidationException("No es posible editar su propio rol");
                }
            }
        }
    }

    public void isAutoUpdate(DetalleUpdateUserEmployeeDTO detalleUpdateUserDTO, String tokenUserActual) {
        UserEntity userToUpdate = userRepository.findById(detalleUpdateUserDTO.id())
                .orElseThrow(()->new ValidationException("El id proporcionado no coincide con ningun registro"));

        UserEntity userActualEntity = userRepository.
                getReferenceByUsername(jwtUtils.getUsernameFromToken(tokenUserActual));

        // Verificamos si los datos para edicion corresponden con el usuario actual
        if(userActualEntity.getIdUsuario().equals(detalleUpdateUserDTO.id())){
            // Verificamos si recibimos rol
            if(detalleUpdateUserDTO.rol()!=null){
                // Verificamos si el rol recibido es diferente al que tenemos actualmente
                if(!userActualEntity.getRol().equals(detalleUpdateUserDTO.rol())){
                    throw new ValidationException("No es posible editar su propio rol");
                }
            }
        }
    }
}