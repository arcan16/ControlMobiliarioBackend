package com.example.controller;

import com.example.dto.recovery.PasswordRecovery;
import com.example.models.UserEntity;
import com.example.respositories.UserRepository;
import com.example.service.email.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@RestController
@RequestMapping("/recover")
public class RecoveryController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Verifica que el usuario/email recibido sean validos y manda manda un enlace al email registrado en
     * la base de datos con un hash de autorizacion que debera usarse para actualizar el password
     * @param data username o email del registro
     * @return mensaje ok para evitar fugas de informacion
     * @throws UnknownHostException
     */
    @GetMapping("/{data}")
    public ResponseEntity<?> recoverPassword(@PathVariable String data) throws UnknownHostException {
        UserEntity user = userRepository.findByUsernameOrEmail(data);
        InetAddress ip = InetAddress.getLocalHost();

        if(user!=null){
            String[] email = {user.getEmail()};
            String code = passwordEncoder
                    .encode(user.getUsername()+user.getEmail()+user.getRol()+user.getPassword());
            emailService.sendEmail(email,"Recuperacion de password","Fue hecha una solicitud para el " +
                    "reestablecimiento de tu contraseña, para actualizar la contraseña dar click al enlace, de lo contrario " +
                    " ignora este mensaje. " +
                    " Enlace de recuperacion: " +"http://"+ip.getHostAddress()+":5173/recovery?param="+ code + "&param2="+user.getUsername());
            System.out.println(code);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Valida el hash recibido para proceder con la actualizacion de la contraseña
     * @param passwordRecovery
     * @return mensaje ok para evitar fugas de informacion
     */
    @PostMapping("/recovery")
    public ResponseEntity<?> recoveryPassword(@RequestBody PasswordRecovery passwordRecovery){
        Optional<UserEntity> user = userRepository.findByUsername(passwordRecovery.username());
        if(user.isPresent()){
            UserEntity data = user.get();
            String code = data.getUsername()+data.getEmail()+data.getRol()+data.getPassword();
            System.out.println(passwordRecovery.authorization());
            System.out.println(code);
            if(passwordEncoder.matches(code, passwordRecovery.authorization())){
                data.setPassword(passwordEncoder.encode(passwordRecovery.newPassword()));
                userRepository.save(data);
            }
        }
        return ResponseEntity.ok().body("{\"message\": \"ok\"}");
    }
}
