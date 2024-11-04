package com.james.vendingmachine.service.serviceImp.Notification;

import com.james.vendingmachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseNotificationListener {

    private final JavaMailSender mailSender;
    private final UserService userService;


    @KafkaListener(topics = "purchase-notifications", groupId = "email-group")
    public void onMessage(String message) {
        try {
            Long userId = extractUserIdFromMessage(message);

            String toEmail = String.valueOf(userService.getUserById(userId));

            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setTo(toEmail);
            emailMessage.setSubject("Product Purchase Notification");
            emailMessage.setText(message);

            mailSender.send(emailMessage);

            System.out.println("Email sent to " + toEmail + " with message: " + message);
        } catch (Exception e) {
            System.err.println("Failed to process message: " + message);
            e.printStackTrace();
        }
    }
    private Long extractUserIdFromMessage(String message) {
        String[] parts = message.split(" ");
        return Long.parseLong(parts[1]);
    }
}
