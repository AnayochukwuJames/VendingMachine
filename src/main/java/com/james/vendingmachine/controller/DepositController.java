package com.james.vendingmachine.controller;

import com.james.vendingmachine.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class DepositController {

    private final UserService userService;

    public DepositController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody Long userId, Integer amount) {
        return userService.deposit(userId, amount);
    }
}