package com.james.vendingmachine.dto;

import com.james.vendingmachine.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseResponse {
    private String message;
    private Integer totalSpent;
    private Product purchasedProduct;
    private List<Integer> change;

    public static PurchaseResponse forBalanceReset() {
        return new PurchaseResponse("Balance: ", 0, null, Collections.emptyList());
    }

    public static PurchaseResponse accessDenied() {
        return new PurchaseResponse(
                "Access denied. Only users with the 'buyer' role can reset their balance.",
                0, null, Collections.emptyList());
    }
}