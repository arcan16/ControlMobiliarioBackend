package com.example.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements IEmailService{

    // Con la anotacion @Bean que le colocamos al MailConfigurations definimos los requisitos necesarios
    // para trabajar con JavaMailSender
    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.sender}")
    private String emailUser;

    @Override
    public void sendEmail(String[] toUser, String subject, String message) {
        // Este objeto nos ayudara a crear nuestro mensaje
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailUser);// Desde cual cuenta vamos a mandar el correo
        mailMessage.setTo(toUser); //A quien o quienes(arreglo de strings con los correos de destino) le vamos a mandar este correo
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    @Override
    public void sendEmailWithFile(String[] toUser, String subject, String message, File file) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            // objeto anterior, se van a adjuntar archivos, tipo de codificacion del archivo que sera enviado
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            mimeMessageHelper.setFrom(emailUser);
            mimeMessageHelper.setTo(toUser);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);
            mimeMessageHelper.addAttachment(file.getName(),file);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
