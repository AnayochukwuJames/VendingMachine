package com.james.vendingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
}