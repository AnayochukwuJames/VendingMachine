package com.james.vendingmachine.controller;

import com.james.vendingmachine.dto.ResetRequest;
import com.james.vendingmachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ResetController {

    private final UserService userService;


    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestBody ResetRequest resetRequest) {
        try {
            userService.resetUser(resetRequest.getUserId());
            return ResponseEntity.ok("User balance reset successfully.");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}

