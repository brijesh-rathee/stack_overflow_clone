package com.stackclone.stackoverflow_clone.service.Impl;

import com.stackclone.stackoverflow_clone.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendNotificationEmail(String to, String title) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Answer Posted");
        message.setText("Someone has posted a new answer on the question: " + title);

        mailSender.send(message);
    }
}
