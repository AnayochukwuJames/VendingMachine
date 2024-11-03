package com.james.vendingmachine.service.serviceImp;

import com.james.vendingmachine.dto.LoginRequest;
import com.james.vendingmachine.dto.LoginResponse;
import com.james.vendingmachine.model.User;
import com.james.vendingmachine.repository.UserRepository;
import com.james.vendingmachine.security.JWTService;
import com.james.vendingmachine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        authenticateUser(loginRequest);
        String jwtToken = jwtService.createToken(user);
        return new LoginResponse(jwtToken, "Login successful");
    }

    private void authenticateUser(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()
            );
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException exception) {
            throw new RuntimeException("Authentication failed: " + exception.getMessage(), exception);
        }
    }

    @Override
    public ResponseEntity<String> logout(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !jwtService.isTokenValid(token, (User) authentication.getPrincipal())) {
            throw new IllegalArgumentException("Invalid or expired token. Logout failed.");
        }
        jwtService.invalidateToken(token);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful.");
    }
}
