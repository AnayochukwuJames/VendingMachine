package com.james.vendingmachine.service;

import com.james.vendingmachine.dto.PurchaseRequest;
import com.james.vendingmachine.dto.PurchaseResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PurchaseService {
    PurchaseResponse buy(PurchaseRequest purchaseRequest) throws MessagingException;

    List<Integer> calculateChange(int balance);

    ResponseEntity<PurchaseResponse> resetBalance(Long userId);
}
