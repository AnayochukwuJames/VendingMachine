package com.james.vendingmachine.exceptionHandler.customException;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
