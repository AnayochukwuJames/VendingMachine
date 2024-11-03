package com.james.vendingmachine.dto;

import com.james.vendingmachine.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseResponse {
    private String message;
    private Integer totalSpent;
    private Product purchasedProduct;
    private List<Integer> change;

    public PurchaseResponse(String message) {
        this.message = message;
    }
}