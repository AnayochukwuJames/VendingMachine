package com.james.vendingmachine.service;

import com.james.vendingmachine.dto.UserRequest;
import com.james.vendingmachine.dto.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<UserResponse> createUser(UserRequest userRequest);

    ResponseEntity<UserResponse> getUserById(Long id);

    ResponseEntity<UserResponse> updateUser(Long id, UserRequest userRequest);

    ResponseEntity<String> deleteUser(Long id);

    ResponseEntity<String> deposit(Long userId, Integer amount);

    void resetUser(Long userId);


}
