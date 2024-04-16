package com.mobiliario.dto.empleados.validations;

import com.mobiliario.dto.empleados.RegistroEmployeeDTO;

public interface EmployeeValidation {
    public void validate(RegistroEmployeeDTO empleado);
    public void validate(String employe);
}
