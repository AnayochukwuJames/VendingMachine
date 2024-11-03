package com.james.vendingmachine.controller;

import com.james.vendingmachine.dto.PurchaseRequest;
import com.james.vendingmachine.dto.PurchaseResponse;
import com.james.vendingmachine.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;


    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/buy")
    public PurchaseResponse buy(@RequestBody PurchaseRequest purchaseRequest){
        return purchaseService.buy(purchaseRequest);

    }
}

