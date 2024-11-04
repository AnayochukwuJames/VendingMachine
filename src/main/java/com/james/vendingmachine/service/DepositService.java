package com.james.vendingmachine.service;

import org.springframework.http.ResponseEntity;

public interface DepositService {
    ResponseEntity<String> deposit(Long userId, Integer amount);
}
