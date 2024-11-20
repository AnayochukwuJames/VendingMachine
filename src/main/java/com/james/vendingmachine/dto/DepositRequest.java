package com.james.vendingmachine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @NotNull(message = "userId must not be null")
    private Long userId;

    @NotNull(message = "Amount must not be null")
    private Integer amount;
}
