package com.james.vendingmachine.service;

import com.james.vendingmachine.dto.PurchaseRequest;
import com.james.vendingmachine.dto.PurchaseResponse;

public interface PurchaseService {
    PurchaseResponse buy(PurchaseRequest purchaseRequest);

}
