package com.example.dto.empleados.validations;

import com.example.dto.empleados.RegistroEmployeeDTO;

public interface EmployeeValidation {
    public void validate(RegistroEmployeeDTO empleado);
    public void validate(String employe);
}
