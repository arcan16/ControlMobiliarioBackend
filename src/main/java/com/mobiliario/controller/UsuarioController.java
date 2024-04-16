package com.mobiliario.controller;

import com.mobiliario.dto.empleados.RegistroEmployeeDTO;
import com.mobiliario.dto.users.*;
import com.mobiliario.models.EmployeeEntity;
import com.mobiliario.models.UserEntity;
import com.mobiliario.respositories.EmployeeRepository;
import com.mobiliario.respositories.UserRepository;
import com.mobiliario.infra.security.jwt.JwtUtils;
import com.mobiliario.service.EmployeeService;
import com.mobiliario.service.users.UserDeleteUpdateValidationService;
import com.mobiliario.service.users.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserDeleteUpdateValidationService userDeleteUpdateValidationService;
    /**
     * Recibe la informacion para crear un nuevo usuario y retorna el detalle de la creacion del registro en caso
     * de que se haya realizado correctamente
     * */
    @PostMapping("/createUser")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid RegistroUsuariosDTO registroUsuariosDTO,
                                              UriComponentsBuilder uriComponentsBuilder){
        if(userRepository.existsByUsername(registroUsuariosDTO.username())){
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonResponse = objectMapper.createObjectNode();
//            System.out.println("El usuario ya existe");
            jsonResponse.put("statusText","El usuario "+registroUsuariosDTO.username()+" ya existe");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        UserEntity userEntity = usuarioService.guardarUsuario(registroUsuariosDTO);
        URI url = uriComponentsBuilder.path("/usuario/getUser/{id}").buildAndExpand(userEntity.getIdUsuario()).toUri();
        DetalleUsuarioDTO detalleUsuarioDTO = new DetalleUsuarioDTO(userEntity);
        return ResponseEntity.created(url).body(detalleUsuarioDTO);
    }
    @PostMapping("/createUserEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> createUserEmployee(@RequestBody @Valid RegistroUsuarioEmpleadoDTO registroUsuarioEmpleadoDTO){
        UserEntity usuario = usuarioService.guardarUsuario(new RegistroUsuariosDTO(registroUsuarioEmpleadoDTO.username(), registroUsuarioEmpleadoDTO.password(),
                registroUsuarioEmpleadoDTO.email(), registroUsuarioEmpleadoDTO.rol()));
//        System.out.println("usuario creado");
        EmployeeEntity employee = employeeService.createEmploye(new RegistroEmployeeDTO(registroUsuarioEmpleadoDTO.nombre(), registroUsuarioEmpleadoDTO.apellido(),
                registroUsuarioEmpleadoDTO.direccion(), registroUsuarioEmpleadoDTO.telefono(), usuario.getIdUsuario()));
//        System.out.println("empleado creado");
        return ResponseEntity.ok().body("{\"message\": \"Usuario creado correctamente \"}");
    }

    @GetMapping("/available/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> isAvailable(@PathVariable @NotNull String username){
        return userRepository.findByUsername(username)
                .map(user-> ResponseEntity.badRequest().body("{\"message\": \"El usuario ya existe\"}"))
                .orElse(ResponseEntity.ok("{\"message\": \"disponible\"}"));

    }

    @GetMapping("/availableue/{username}/{nombre}/{apellido}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> isAvailableUE(@PathVariable @NotNull String username, @PathVariable @NotNull String nombre,
                                           @PathVariable @NotNull String apellido){
        Optional<UserEntity> user = userRepository.findByUsername(username);
        Optional<EmployeeEntity> employee = employeeRepository.findByNombreApellido(nombre, apellido);
//        System.out.println(user);
//        System.out.println(employee);

        if(user.isPresent() && employee.isPresent())return ResponseEntity.badRequest()
                .body("{\"message\": \"El Usuario y el empleado ya existen\"}");

        if(user.isPresent() && employee.isEmpty())return ResponseEntity.badRequest()
                .body("{\"message\": \"El Usuario ya existe\"}");

        if(user.isEmpty() && employee.isPresent())return ResponseEntity.badRequest()
                .body("{\"message\": \"El Empleado ya existe\"}");

//        if(user.isPresent()) return ResponseEntity.badRequest().body("{\"message\": \"El Usuario ya existe\"}");
//        if(employee.isPresent()) return ResponseEntity.badRequest().body("{\"message\": \"El Empleado ya existe\"}");
        return ResponseEntity.ok().body("{\"message\": \"disponible \"}");
    }
    /**
     * Retorna una página con todos los usuarios registrados en la entidad UserEntity
     * @param page crea una pagina con 10 registros (en caso de que existan)
     * */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> allUsers(@PageableDefault(size = 50) Pageable page){
        return ResponseEntity.ok(userRepository.findAll(page).map(DetalleCompletoUsersDTO::new));
    }

    /**
    * Retorna la información del registro de la entidad UserEntity en caso de existir, de lo contrario
    * lanza una excepcion
    * @param id identificador del registro que se va a consultar
    * */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSingleUser(@PathVariable @NotNull Long id){
        UserEntity singleUser = userRepository.findById(id)
                .orElseThrow(()-> new ValidationException("El id proporcionado no coincide con ningun usuario"));

        return ResponseEntity.ok(new DetalleUsuarioDTO(singleUser));
    }

    /**
     * Busca en la base de datos los datos del registro utilizando el token recibido
     * @param httpServletRequest
     * @return Json con la informacion del usuario
     */
    @GetMapping("/actual")
    public ResponseEntity<UserEntity> getActualUser(HttpServletRequest httpServletRequest){
        UserEntity actualUser = usuarioService.getActualUser(httpServletRequest);
        return ResponseEntity.ok(actualUser);
    }


    /**
     * Recibe como parametro el id del registro que se desea eliminar
     * Si se trata de un usuario ADMIN no es posible eliminarse a si mismo
     * */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> oneUser(@PathVariable @NotNull Long id, HttpServletRequest request){
        String userName = jwtUtils.getUsernameFromToken(jwtUtils.getTokenFromHeader(request));

        userDeleteUpdateValidationService.isAutoDelete(userName,id);

        UserEntity userToDelete =(UserEntity) userRepository.findById(id)
                .orElseThrow(()-> new ValidationException("El id proporcionado no coincide con ningun usuario"));
        userRepository.deleteById(userToDelete.getIdUsuario());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delteUserEmployee/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserEmployee(@PathVariable Long id){
        UserEntity user = userRepository.findById(id).orElseThrow(()->new ValidationException("El id proporcionado no existe"));
        EmployeeEntity employee = employeeRepository.findByIdUsuario(id);
        if(employee!=null){
            employeeRepository.delete(employee);
        }
        userRepository.delete(user);
        return ResponseEntity.ok().body("{\"message\": \"Datos eliminados exitosamente\"}");
    }

    /**
     * Recibe la informacion del registro que se desea actualizar para cualquier usuario
     * @return
     */
    @PutMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserAdmin(@RequestBody @Valid DetalleCompletoUsersDTO detalleCompletoUsersDTO,
                                             HttpServletRequest request){
        // Validaciones
        userDeleteUpdateValidationService.isAutoUpdate(detalleCompletoUsersDTO,
                request.getHeader("Authorization").substring(7));
        // Actualizacion
        UserEntity usuarioActualizado = usuarioService.update(detalleCompletoUsersDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();

        jsonResponse.put("message","Registro actualizado");

        return ResponseEntity.ok().body(jsonResponse);
    }

    /**
     * Actualiza los datos del usuario
     * @param detalleUpdateUserDTO
     * @param request
     * @return
     */
    @PutMapping("/update")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody @Valid DetalleUpdateUserDTO detalleUpdateUserDTO,
                                             HttpServletRequest request){
        // Validaciones
        userDeleteUpdateValidationService.isAutoUpdate(detalleUpdateUserDTO,
                request.getHeader("Authorization").substring(7));
        // Actualizacion de los datos del usuario
        // Nota: al actualizar el "username" se debera logear nuevamente para generar nuevamente el token
//        System.out.println("Vamos a actualizar la informacion");
        UserEntity user = usuarioService.updateUser(detalleUpdateUserDTO);
        return ResponseEntity.ok().body("{\"message\": \"ok\"}");
    }

    /**
     * Actualiza los datos del usuario y los del empleado
     * @param detalleUpdateUserDTO
     * @param request
     * @return
     */
    @PutMapping("/updateComplete")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserEmployee(@RequestBody @Valid DetalleUpdateUserEmployeeDTO detalleUpdateUserDTO,
                                        HttpServletRequest request){
//        System.out.println("Actualizando datos completos");
//        System.out.println(detalleUpdateUserDTO);
        // Validaciones
        userDeleteUpdateValidationService.isAutoUpdate(detalleUpdateUserDTO,
                request.getHeader("Authorization").substring(7));
        // Actualizacion de los datos del usuario
        // Nota: al actualizar el "username" se debera logear nuevamente para generar nuevamente el token
        UserEntity user = usuarioService.updateUser(detalleUpdateUserDTO);

        EmployeeEntity employee = employeeService.employeeUpdate(detalleUpdateUserDTO, user);
        // Verificamos si se trata del mismo usuario que ya teniamos
            //Si es el mismo, actualizamos los demas registros, omitiendo el nombre de usuario
        // Si no se trata del mismo usuario que ya teniamos registrado
            // Verificamos la disponibilidad del usuario
                // En caso de que no este disponible el nuevo nombre de usuario, regresamos error
                // En caso de que este disponible el nombre de usuario recibido, actualizamos los datos del registro

        // Verificamos si el usuario tiene datos de empleado
            // Si los tiene, los actualizamos
                // Si recibimos el mismo nombre-apellido que ya tiene registrado solo actualizamos los demas datos
                // Si no son los mismos, verificamos que esten disponibles
                    // Si esta disponible, actualizamos el registro
                    // Si no estan disponibles, regresamos error
            // Si no los tiene, creamos el registro del empleado con el id del usuario recibido
        return ResponseEntity.ok().body("{\"message\": \"ok\"}");
    }
}