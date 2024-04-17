package com.example.controller;

import com.example.dto.email.EmailDTO;
import com.example.dto.email.EmailFileDTO;
import com.example.service.email.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class MailController {

    @Autowired
    private IEmailService emailService;

    /**
     * Envia un email utilizando los parametros recibidos
     * @param emailDTO
     * @return
     */
    @PostMapping("/sendMessage")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDTO emailDTO){
        System.out.println("Mensaje Recibido " + emailDTO);

        emailService.sendEmail(emailDTO.toUser(), emailDTO.subject(), emailDTO.message());

        Map<String, String> response = new HashMap<>();
        response.put("estado","Enviado");

        return ResponseEntity.ok(response);
    }

    // Con la anotacion @ModelAttribure podemos trabajar con archivos

    /**
     * Envia un email utilizando los parametros recibidos incluyendo archivos
     * @param emailFileDTO
     * @return
     */
    @PostMapping("/sendFile")
    public ResponseEntity<?> receiveRequestEmailWithFile(@ModelAttribute EmailFileDTO emailFileDTO){

        try {
            String fileName = emailFileDTO.file().getName();

            Path  path = Paths.get("src/main/resources/file/"+fileName);

            Files.createDirectories(path.getParent());// Si no existe la ruta, la creamos
            Files.copy(emailFileDTO.file().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); // Si el archivo ya existe, reemplazalo

            File file = path.toFile(); // Vamos a referencia nuestro archivo

            emailService.sendEmailWithFile(emailFileDTO.toUser(), emailFileDTO.subject(), emailFileDTO.message(), file);

            Map<String, String> response = new HashMap<>();
            response.put("estado","Enviado");
            response.put("archivo","fileName");

            return ResponseEntity.ok().build();

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el Email con el archivo " + e.getMessage());
        }
    }
}
