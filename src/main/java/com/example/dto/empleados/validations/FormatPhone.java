package com.example.dto.empleados.validations;

import com.example.dto.empleados.RegistroEmployeeDTO;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class FormatPhone implements EmployeeValidation{
    @Override
    public void validate(RegistroEmployeeDTO empleado) {
        String regex = "[0-9]{10}";

        Pattern pattern = Pattern.compile(regex);

        if(!pattern.matcher(empleado.telefono()).matches()){
            throw new ValidationException("El campo telefono solo recibe 10 digitos," +
                    " favor de verificar");
        }
    }

    @Override
    public void validate(String telefono) {
        String regex = "[0-9]{10}";

        Pattern pattern = Pattern.compile(regex);

        if(!pattern.matcher(telefono).matches()){
            throw new ValidationException("El campo telefono solo recibe 10 digitos," +
                    " favor de verificar");
        }
    }
}
