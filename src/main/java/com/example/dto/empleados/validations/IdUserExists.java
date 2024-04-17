package com.example.dto.empleados.validations;

import com.example.dto.empleados.RegistroEmployeeDTO;
import com.example.respositories.UserRepository;
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
