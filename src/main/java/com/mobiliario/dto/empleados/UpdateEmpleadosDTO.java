package com.mobiliario.dto.empleados;

import com.mobiliario.models.EmployeeEntity;
import jakarta.validation.constraints.NotNull;

public record UpdateEmpleadosDTO(@NotNull Long id,
                                 String nombre,
                                 String apellido,
                                 String direccion,
                                 String telefono) {
    public UpdateEmpleadosDTO(EmployeeEntity employee){
        this(employee.getIdEmpleado(), employee.getNombre(), employee.getApellido(), employee.getDireccion(),
                employee.getTelefono());
    }
}
