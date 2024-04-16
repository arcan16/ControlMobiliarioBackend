package com.mobiliario.dto.empleados.validations;

import com.mobiliario.dto.empleados.RegistroEmployeeDTO;
import com.mobiliario.respositories.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdUserExists implements  EmployeeValidation{
    @Autowired
    UserRepository userRepository;

    @Override
    public void validate(RegistroEmployeeDTO empleado) {
        userRepository.findById(empleado.idUsuario()).
                orElseThrow(()->new ValidationException("El usuario no existe"));
    }

    @Override
    public void validate(String employe) {

    }
}
