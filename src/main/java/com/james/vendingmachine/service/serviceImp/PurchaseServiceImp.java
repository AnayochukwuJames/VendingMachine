package com.james.vendingmachine.service.serviceImp;

import com.james.vendingmachine.dto.PurchaseRequest;
import com.james.vendingmachine.dto.PurchaseResponse;
import com.james.vendingmachine.model.Product;
import com.james.vendingmachine.model.User;
import com.james.vendingmachine.repository.ProductRepository;
import com.james.vendingmachine.repository.UserRepository;
import com.james.vendingmachine.service.PurchaseService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PurchaseServiceImp implements PurchaseService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PurchaseServiceImp(UserRepository userRepository, ProductRepository productRepository,
                              KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public PurchaseResponse buy(PurchaseRequest purchaseRequest) {
        Long userId = purchaseRequest.getUserId();
        Long productId = purchaseRequest.getProductId();
        Integer quantity = purchaseRequest.getQuantity();

        if (quantity > 1) {
            throw new IllegalArgumentException("You can only purchase one product at a time.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
        int totalCost = (int) (product.getPrice() * quantity);

        if (user.getBalance() < totalCost) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        user.setBalance(user.getBalance() - totalCost);
        userRepository.save(user);

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        String notificationMessage = "User " + userId + " purchased " + quantity + " of product " + productId;
        kafkaTemplate.send("product-sold", notificationMessage);

        List<Integer> change = calculateChange((int) user.getBalance());

        return new PurchaseResponse("Purchase successful.", totalCost, product, change);
    }


    private List<Integer> calculateChange(int balance) {
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
}
