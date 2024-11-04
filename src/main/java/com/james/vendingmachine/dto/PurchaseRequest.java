package com.james.vendingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
public class PurchaseRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
}