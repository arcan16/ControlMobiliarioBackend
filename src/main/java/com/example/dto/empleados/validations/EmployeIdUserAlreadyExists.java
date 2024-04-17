package com.example.dto.empleados.validations;

import com.example.dto.empleados.RegistroEmployeeDTO;
import com.example.models.UserEntity;
import com.example.respositories.EmployeeRepository;
import com.example.respositories.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeIdUserAlreadyExists implements EmployeeValidation{
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(RegistroEmployeeDTO empleado) {
        UserEntity userEntity = userRepository.findById(empleado.idUsuario())
                .orElseThrow(()->new ValidationException("El usuario no existe"));

        if(employeeRepository.findByUsuario(userEntity.getIdUsuario()).isPresent()){
            throw new ValidationException("El usuario ya tiene datos de empleado registrados");
        }
    }

    @Override
    public void validate(String employe) {

    }
}
