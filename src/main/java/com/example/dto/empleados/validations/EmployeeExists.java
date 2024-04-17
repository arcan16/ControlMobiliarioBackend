package com.example.dto.empleados.validations;

import com.example.dto.empleados.RegistroEmployeeDTO;
import com.example.respositories.EmployeeRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeExists implements EmployeeValidation {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void validate(RegistroEmployeeDTO empleado) {
        if(employeeRepository.existsByNameLastname(empleado.nombre(),empleado.apellido())>0){
            throw new ValidationException("El empleado ya existe, no es posible duplicarlo");
        }
    }

    @Override
    public void validate(String employe) {

    }
}
