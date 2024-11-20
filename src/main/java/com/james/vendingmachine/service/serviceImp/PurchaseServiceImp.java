package com.james.vendingmachine.service.serviceImp;

import com.james.vendingmachine.dto.PurchaseRequest;
import com.james.vendingmachine.dto.PurchaseResponse;
import com.james.vendingmachine.model.Product;
import com.james.vendingmachine.model.User;
import com.james.vendingmachine.repository.ProductRepository;
import com.james.vendingmachine.repository.UserRepository;
import com.james.vendingmachine.service.PurchaseService;
import com.james.vendingmachine.service.serviceImp.Notification.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImp implements PurchaseService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NotificationService emailService;

    @Override
    public PurchaseResponse buy(PurchaseRequest purchaseRequest) throws MessagingException {
        validatePurchaseRequest(purchaseRequest);

        User user = getAuthenticatedUser();
        Product product = getProduct(purchaseRequest.getProductId());
        int totalCost = calculateTotalCost(product, purchaseRequest.getQuantity());
        if (user.getBalance() < totalCost) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        processPurchase(user, product, totalCost, purchaseRequest.getQuantity());
        sendPurchaseNotification(user.getId(), product.getId(), purchaseRequest.getQuantity());

        emailService.sendPurchaseNotification(user.getId(), product.getId(), purchaseRequest.getQuantity(), user.getUsername());

        List<Integer> change = calculateChange((int) user.getBalance());
        return new PurchaseResponse("Product purchase successful.", totalCost, product, change);
    }

    private void validatePurchaseRequest(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getQuantity() != 1) {
            throw new IllegalArgumentException("Only one product can be purchased at a time.");
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
    }

    private int calculateTotalCost(Product product, int quantity) {
        return (int) (product.getPrice() * quantity);
    }

    private void processPurchase(User user, Product product, int totalCost, int quantity) {
        user.setBalance(user.getBalance() - totalCost);
        userRepository.save(user);

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    private void sendPurchaseNotification(Long userId, Long productId, int quantity) {
        String notificationMessage = String.format("User %d purchased %d of product %d", userId, quantity, productId);
        kafkaTemplate.send("product-sold", notificationMessage);
    }

    @Override
    public List<Integer> calculateChange(int balance) {
        List<Integer> denominations = Arrays.asList(1000, 500, 200, 100, 50);
        List<Integer> change = new ArrayList<>();

        for (int denomination : denominations) {
            while (balance >= denomination) {
                balance -= denomination;
                change.add(denomination);
            }
        }
        return change;
    }

    @Override
    public ResponseEntity<PurchaseResponse> resetBalance(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isBuyer = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_BUYER"));

        if (!isBuyer) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(PurchaseResponse.accessDenied());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        user.setBalance(0);
        userRepository.save(user);
        return ResponseEntity.ok(PurchaseResponse.forBalanceReset());
    }
}
