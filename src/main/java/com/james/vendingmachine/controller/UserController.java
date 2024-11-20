package com.james.vendingmachine.controller;

import com.james.vendingmachine.dto.UserRequest;
import com.james.vendingmachine.dto.UserResponse;
import com.james.vendingmachine.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) throws MessagingException {
        return userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("email")
    public String getUserByEmail(@RequestParam String email) {
        return userService.getUsername(email);
    }
}