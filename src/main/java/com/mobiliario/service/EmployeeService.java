package com.mobiliario.service;

import com.mobiliario.dto.empleados.RegistroEmployeeDTO;
import com.mobiliario.dto.empleados.UpdateEmpleadosDTO;
import com.mobiliario.dto.empleados.validations.EmployeNameOnlyCharacters;
import com.mobiliario.dto.empleados.validations.EmployeeValidation;
import com.mobiliario.dto.empleados.validations.FormatPhone;
import com.mobiliario.dto.users.DetalleUpdateUserEmployeeDTO;
import com.mobiliario.models.EmployeeEntity;
import com.mobiliario.models.UserEntity;
import com.mobiliario.respositories.EmployeeRepository;
import com.mobiliario.respositories.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeNameOnlyCharacters employeNameOnlyCharacters;

    @Autowired
    private FormatPhone formatPhone;

    @Autowired
    private List<EmployeeValidation> validador = new ArrayList<>();

    public EmployeeEntity createEmploye(RegistroEmployeeDTO employee){
        validador.forEach(v->v.validate(employee));
        UserEntity userEntity = userRepository.getReferenceById(employee.idUsuario());
        EmployeeEntity employeeEntity = new EmployeeEntity(employee,userEntity);
        return employeeRepository.save(employeeEntity);
    }

    /**
     * Actualiza la informacion del registro con los datos recibidos en el PUT
     * @param empleado
     */
    public void update(UpdateEmpleadosDTO empleado) {

        EmployeeEntity employeeEntity = employeeRepository.getReferenceById(empleado.id());
        if(empleado.nombre()!=null && !empleado.nombre().isBlank()){

            employeNameOnlyCharacters.validate(empleado.nombre());
            employeeEntity.setNombre(empleado.nombre());
        }
        if(empleado.apellido()!=null && !empleado.apellido().isBlank()){
            employeNameOnlyCharacters.validate(empleado.apellido());
            employeeEntity.setApellido(empleado.apellido());
        }
        if(empleado.direccion()!=null && !empleado.direccion().isBlank()){
            employeeEntity.setDireccion(empleado.direccion());
        }
        if(empleado.telefono()!=null && !empleado.telefono().isBlank()){
            formatPhone.validate(empleado.telefono());
            employeeEntity.setTelefono(empleado.telefono());
        }
        employeeRepository.save(employeeEntity);
    }

    public EmployeeEntity employeeUpdate(DetalleUpdateUserEmployeeDTO dataUserEmployee, UserEntity user){
        EmployeeEntity employee = employeeRepository.findByIdUsuario(dataUserEmployee.id());

        // Si tenemos employee se trata de una actualizacion de datos
        if(employee!=null){
            System.out.println("El empleado ya existia");
            if(!employee.getNombre().equals(dataUserEmployee.nombre())
                    || !employee.getApellido().equals(dataUserEmployee.apellido())){

                // verificamos la disponibilidad de nombre y apellido
                Optional<EmployeeEntity> employeeAvailable = employeeRepository
                        .findByNombreApellido(dataUserEmployee.nombre(), dataUserEmployee.apellido());
                    // Si no esta disponible regresamos un error
                if(employeeAvailable.isPresent())throw new ValidationException("El nombre y apellido del usuario ya" +
                        " se encuentan registrados. No es posible registrarlo nuevamente");
            }
            // Si esta disponible actualizamos la informacion
            System.out.println("Actualizando informacion");
            employee.setNombre(dataUserEmployee.nombre());
            employee.setApellido(dataUserEmployee.apellido());
            employee.setDireccion(dataUserEmployee.direccion());
            employee.setTelefono(dataUserEmployee.telefono());
//            employeeRepository.save(employee);

        }else{
            // Si no tenemos employee se trata de una creacion de registro
            Optional<EmployeeEntity> employeeAvailable = employeeRepository
                    .findByNombreApellido(dataUserEmployee.nombre(), dataUserEmployee.apellido());
            if(employeeAvailable.isPresent())throw new ValidationException("El nombre y apellido del usuario ya" +
                    "se encuentan registrados. No es posible registrarlo nuevamente");
            // Verificamos la disponibilidad de nombre y apellido
                // Si esta disponible actualizamos la informacion
                // Si no esta disponible regresamos un error
            EmployeeEntity newEmployee = new EmployeeEntity();
            newEmployee.setNombre(dataUserEmployee.nombre());
            newEmployee.setApellido(dataUserEmployee.apellido());
            newEmployee.setDireccion(dataUserEmployee.direccion());
            newEmployee.setTelefono(dataUserEmployee.telefono());
            newEmployee.setUsuario(user);
            employeeRepository.save((newEmployee));
        }




        return new EmployeeEntity();
    }
}
