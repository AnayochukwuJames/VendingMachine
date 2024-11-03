package com.james.vendingmachine.exceptionHandler.customException;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
