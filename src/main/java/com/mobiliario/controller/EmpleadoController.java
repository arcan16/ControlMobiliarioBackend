package com.mobiliario.controller;

import com.mobiliario.dto.empleados.DetalleCompletoEmpleadosDTO;
import com.mobiliario.dto.empleados.RegistroEmployeeDTO;
import com.mobiliario.dto.empleados.UpdateEmpleadosDTO;
import com.mobiliario.infra.security.jwt.JwtUtils;
import com.mobiliario.models.EmployeeEntity;
import com.mobiliario.models.UserEntity;
import com.mobiliario.respositories.EmployeeRepository;
import com.mobiliario.respositories.UserRepository;
import com.mobiliario.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/employees")
public class EmpleadoController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    /**
     * Recibe datos para crear un registro en de la entidad EmployeeEntity (tabla empleados)
     * @param registroEmployeeDTO
     * @return
     */
    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createEmployee(@RequestBody @Valid RegistroEmployeeDTO registroEmployeeDTO,
                                            UriComponentsBuilder uriComponentsBuilder){
        EmployeeEntity employeeEntity = employeeService.createEmploye(registroEmployeeDTO);
        URI url = uriComponentsBuilder.path("/employee/{id}").buildAndExpand(employeeEntity.getIdEmpleado()).toUri();
        return ResponseEntity.created(url).body(registroEmployeeDTO);
    }

    /**
     * Regresa todos los registros de la entidad EmployeeEntity (tabla empleados)
     * @param myPage
     * @return
     */
    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DetalleCompletoEmpleadosDTO>> getAllEmployees(
            @PageableDefault(size = 10)Pageable myPage){
        return ResponseEntity.ok(employeeRepository.findAll(myPage).map(DetalleCompletoEmpleadosDTO::new));
    }

    /**
     * Regresa la informacion registrada de un id de empleado siempre y cuando la peticion sea realizada por
     * un usuario de tipo ADMIN
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getEmployee(@PathVariable @NotNull Long id, Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))){
            return ResponseEntity.ok(employeeRepository.findById(id));
        }
        return ResponseEntity.ok(authentication.getAuthorities());
    }

    /**
     * Regresa la informacion de empmleado del usuario que realiza la peticion
     * @param request
     * @return
     */
    @GetMapping("/myData/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getEmployeeUser(HttpServletRequest request,
                                             @PathVariable String username){
//        String token = jwtUtils.getTokenFromHeader(request);
//        String username = jwtUtils.getUsernameFromToken(token);
        UserEntity userEntity = userRepository.getReferenceByUsername(username);
        EmployeeEntity employeeEntity = employeeRepository.findByUsuario(userEntity.getIdUsuario())
                .orElseThrow(()->new ValidationException("Aun no tienes datos registrados"));
        /* Este codigo nos sirve para obtener el rol de la peticion, solo debemos agregar
         a los parametros Authentication authentication
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))){
            return ResponseEntity.ok(employeeRepository.findById(id));
        }else {
        }*/
        return ResponseEntity.ok(new DetalleCompletoEmpleadosDTO(employeeEntity));
    }

    /**
     * Recibe los datos del registrso que se desea actualizar de la entidad EmployeeEntity
     * @param updateEmpleadosDTO
     * @return
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateEmployee(@RequestBody @Valid UpdateEmpleadosDTO updateEmpleadosDTO){
        employeeService.update(updateEmpleadosDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        jsonResponse.put("message","Registro actualizado");
        return ResponseEntity.ok().body(jsonResponse);
    }

    /**
     * Elimina un registro de la entidad EmployeeEntity (tabla empleados) utilizando el id
     * recibido en la url
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmploye(@PathVariable Long id){
        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(()->new ValidationException("El id proporcionado no existe"));
        employeeRepository.delete(employeeEntity);
        return ResponseEntity.noContent().build();
    }
}