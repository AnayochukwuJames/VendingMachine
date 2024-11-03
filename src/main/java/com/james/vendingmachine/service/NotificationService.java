package com.james.vendingmachine.service;

import com.james.vendingmachine.model.User;
import com.james.vendingmachine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final UserRepository userRepository;


    @KafkaListener(topics = "product-sold", groupId = "email-group")
    public void listen(String message) {
        try {
            String[] parts = message.split(",");
            Long userId = Long.valueOf(parts[0]);

            String userEmail = userRepository.findById(userId)
                    .map(User::getUsername)
                    .orElseThrow(() -> new IllegalArgumentException("User not found."));

            emailService.sendEmail(userEmail, "Product Sold", message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
