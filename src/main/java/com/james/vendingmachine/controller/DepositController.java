package com.james.vendingmachine.controller;

import com.james.vendingmachine.dto.DepositRequest;
import com.james.vendingmachine.dto.DepositResponse;
import com.james.vendingmachine.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(@RequestBody DepositRequest depositRequest) {

        return depositService.deposit(depositRequest.getUserId(), depositRequest.getAmount());
    }
}