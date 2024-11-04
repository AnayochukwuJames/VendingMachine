package com.james.vendingmachine.service;

import com.james.vendingmachine.dto.UserRequest;
import com.james.vendingmachine.dto.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<UserResponse> createUser(UserRequest userRequest) throws MessagingException;

    ResponseEntity<UserResponse> getUserById(Long id);

    ResponseEntity<UserResponse> updateUser(Long id, UserRequest userRequest);

    ResponseEntity<String> deleteUser(Long id);

}
