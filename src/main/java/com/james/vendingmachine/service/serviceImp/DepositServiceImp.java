package com.james.vendingmachine.service.serviceImp;

import com.james.vendingmachine.dto.DepositResponse;
import com.james.vendingmachine.model.User;
import com.james.vendingmachine.repository.UserRepository;
import com.james.vendingmachine.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositServiceImp implements DepositService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<DepositResponse> deposit(Long userId, Integer amount) {
        List<Integer> validNotes = Arrays.asList(50, 100, 200, 500, 1000);
        if (!validNotes.contains(amount)) {
            return ResponseEntity.badRequest()
                    .body(new DepositResponse("Invalid note value. Valid values are: 50, 100, 200, 500, 1000.", null));
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isBuyer = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("BUYER"));
        if (!isBuyer) {
            return ResponseEntity.status(403)
                    .body(new DepositResponse("Only users with the buyer role can make deposits.", null));
        }
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return ResponseEntity.ok(new DepositResponse("Deposit successful.", user.getBalance()));
    }


}
