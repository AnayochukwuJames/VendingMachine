package com.james.vendingmachine.service;

import com.james.vendingmachine.dto.DepositResponse;
import org.springframework.http.ResponseEntity;

public interface DepositService {
    ResponseEntity<DepositResponse> deposit(Long userId, Integer amount);
}
