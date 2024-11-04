package com.james.vendingmachine.controller;

import com.james.vendingmachine.dto.PurchaseRequest;
import com.james.vendingmachine.dto.PurchaseResponse;
import com.james.vendingmachine.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;


    @PostMapping("/buy-product")
    public PurchaseResponse buyProduct(@RequestBody PurchaseRequest purchaseRequest){
        return purchaseService.buy(purchaseRequest);
    }

    @PostMapping("/reset")
    public ResponseEntity<PurchaseResponse> resetUserBalance(@PathVariable Long userId) {
        return purchaseService.resetBalance(userId);
    }

    @GetMapping("/calculate-change")
    public ResponseEntity<List<Integer>> calculateChange(@RequestParam int balance) {
        return (ResponseEntity<List<Integer>>) purchaseService.calculateChange(balance);
    }
}

