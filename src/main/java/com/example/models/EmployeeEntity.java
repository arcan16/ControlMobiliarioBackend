package com.example.models;

import com.example.dto.empleados.RegistroEmployeeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "EmployeeEntity")
@Table(name = "empleados")
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(id)
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private UserEntity usuario;

    public EmployeeEntity(RegistroEmployeeDTO employee) {
        this.nombre=employee.nombre();
        this.apellido=employee.apellido();
        this.direccion=employee.direccion();
        this.telefono=employee.telefono();
    }

    public EmployeeEntity(RegistroEmployeeDTO employee, UserEntity userEntity) {
        this.nombre = employee.nombre();
        this.apellido = employee.apellido();
        this.direccion = employee.direccion();
        this.telefono = employee.telefono();
        this.usuario = userEntity;
    }
}
