package com.mobiliario.dto.empleados.validations;

import com.mobiliario.dto.empleados.RegistroEmployeeDTO;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmployeNameOnlyCharacters implements EmployeeValidation{
    @Override
    public void validate(RegistroEmployeeDTO employee) {
        String regex = "^[a-zA-ZÑñ ]{1,}";

        Pattern pattern = Pattern.compile(regex);
        System.out.println("Nombre correcto? "+pattern.matcher(employee.nombre()).matches());
        System.out.println("Apellido correcto? "+pattern.matcher(employee.apellido()).matches());

        if(!pattern.matcher(employee.nombre()).matches() ||
                !pattern.matcher(employee.apellido()).matches()){

            throw new ValidationException("El nombre y apellido solo aceptan letras y espacios, verificalos " +
                    "antes de continuar");
        }
    }
    @Override
    public void validate(String employe) {
        String regex = "^[a-zA-ZÑñ ]{1,}";

        Pattern pattern = Pattern.compile(regex);

        if(!pattern.matcher(employe).matches()){

            throw new ValidationException("El nombre y apellido solo aceptan letras y espacios, verificalos " +
                    "antes de continuar");
        }
    }
}
