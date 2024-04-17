package com.example.dto.empleados;

import com.example.models.EmployeeEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DetalleCompletoEmpleadosDTO(@NotNull Long id,
                                          @NotNull @NotBlank String nombre,
                                          @NotNull @NotBlank String apellido,
                                          @NotNull @NotBlank String direccion,
                                          @NotNull @NotBlank String telefono,
                                          @NotNull Long idUsuario) {
    public DetalleCompletoEmpleadosDTO (EmployeeEntity employee){
        this(employee.getIdEmpleado(), employee.getNombre(), employee.getApellido(), employee.getDireccion(),
                employee.getTelefono(), employee.getUsuario().getIdUsuario());
    }
}
