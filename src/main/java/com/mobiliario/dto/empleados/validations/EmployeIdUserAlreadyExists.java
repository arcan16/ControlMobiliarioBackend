package com.mobiliario.dto.empleados.validations;

import com.mobiliario.dto.empleados.RegistroEmployeeDTO;
import com.mobiliario.models.UserEntity;
import com.mobiliario.respositories.EmployeeRepository;
import com.mobiliario.respositories.UserRepository;
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
