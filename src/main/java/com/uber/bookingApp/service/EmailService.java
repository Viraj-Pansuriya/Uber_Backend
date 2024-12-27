package com.uber.bookingApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;



    public void sendEmail(String to, String subject, String message) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom("virajpansuriya777@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true); // The second parameter indicates HTML content
        mailSender.send(mimeMessage);

    }
}