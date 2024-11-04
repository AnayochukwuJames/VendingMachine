package com.james.vendingmachine.service;

import com.james.vendingmachine.dto.LoginRequest;
import com.james.vendingmachine.dto.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    ResponseEntity<String> logout(String token);

    ResponseEntity<String> logoutAll(Long userId);
}
