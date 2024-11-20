package com.james.vendingmachine.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotNull(message = "Product ID must not be null")
    private Long productId;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
}
