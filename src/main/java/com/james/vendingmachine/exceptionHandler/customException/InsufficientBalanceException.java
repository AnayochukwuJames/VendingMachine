package com.james.vendingmachine.exceptionHandler.customException;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
