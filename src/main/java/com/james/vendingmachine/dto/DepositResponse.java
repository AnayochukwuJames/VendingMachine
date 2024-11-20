package com.james.vendingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepositResponse {
    private String message;
    private Integer newBalance;
}
