package com.mobiliario.service.email;

import java.io.File;

public interface IEmailService {
    //Envia mensajes solo con texto
    void sendEmail(String[] toUser, String subject, String message);

    // Permite adjuntar archivos al correo
    void sendEmailWithFile(String[] toUser, String subject, String message, File file);

}
