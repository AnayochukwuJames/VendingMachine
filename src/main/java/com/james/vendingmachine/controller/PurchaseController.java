package com.james.vendingmachine.controller;

import com.james.vendingmachine.dto.DepositRequest;
import com.james.vendingmachine.dto.DepositResponse;
import com.james.vendingmachine.dto.PurchaseRequest;
import com.james.vendingmachine.dto.PurchaseResponse;
import com.james.vendingmachine.service.DepositService;
import com.james.vendingmachine.service.PurchaseService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;


    private final DepositService depositService;

    @PostMapping("/buy-product")
    public PurchaseResponse buyProduct(@RequestBody PurchaseRequest purchaseRequest) throws MessagingException {
        return purchaseService.buy(purchaseRequest);
    }

    @PostMapping("/reset{id}")
    public ResponseEntity<PurchaseResponse> resetUserBalance(@PathVariable Long userId) {
        return purchaseService.resetBalance(userId);
    }

    @GetMapping("/calculate-change")
    public ResponseEntity<List<Integer>> calculateChange(@RequestParam int balance) {
        return (ResponseEntity<List<Integer>>) purchaseService.calculateChange(balance);
    }
}

