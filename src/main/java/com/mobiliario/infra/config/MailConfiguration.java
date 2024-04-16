package com.mobiliario.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Value("${email.sender}")
    private String emailUser;

    @Value("${email.password}")
    private String password;

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailUser);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp"); // Define el protocolo de comunicacion entre nuestra app y el servidor
        props.put("mail.smtp.auth", "true");// Habilita la authenticacion
        props.put("mail.smtp.starttls.enable", "true");// Habilita el cifrado en la comunicacion
        props.put("mail.debug", "true");//Nos va a imprimier la informacion sobre la comunicacion entre nuestra aplicacion y el servidor



        return mailSender;
    }
}
