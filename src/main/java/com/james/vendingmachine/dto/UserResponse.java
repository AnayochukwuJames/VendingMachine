package com.james.vendingmachine.dto;

import com.james.vendingmachine.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
        private String message;
        private User user;
    }


