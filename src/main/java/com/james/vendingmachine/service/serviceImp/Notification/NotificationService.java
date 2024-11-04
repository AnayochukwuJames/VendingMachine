package com.james.vendingmachine.service.serviceImp.Notification;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
        } catch (MailException e) {
            // Log the exception or handle the error as necessary
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendPurchaseNotification(Long userId, Long productId, int quantity, String email) {
        String messageText = String.format("User %d purchased %d of product %d", userId, quantity, productId);

        kafkaTemplate.send("purchase-notifications", messageText);

        if (email != null && !email.isEmpty()) {
            sendEmail(email, "Product Purchase Notification", messageText);
        }
    }

    @Async
    public void sendRegistrationNotification(String firstName, String email) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        messageHelper.setTo(email);
        messageHelper.setSubject("Welcome to the Vending Machine App");
        String message = String.format("Hello %s,\n\nYou have successfully registered to the Vending Machine App. Please proceed to login.", firstName);
        messageHelper.setText(message);
        mailSender.send(mimeMessage);
    }
}
