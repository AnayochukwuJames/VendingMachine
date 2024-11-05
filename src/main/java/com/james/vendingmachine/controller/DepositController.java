package com.james.vendingmachine.controller;

import com.james.vendingmachine.service.DepositService;
import com.james.vendingmachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deposit")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody Long userId, Integer amount) {
        return depositService.deposit(userId, amount);
    }
}