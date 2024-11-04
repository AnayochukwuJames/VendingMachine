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

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    private final Map<String, Long> activeSessions = new HashMap<>();

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        if (activeSessions.containsValue(user.getId())) {
            throw new RuntimeException("There is already an active session using your account. Please logout to login again.");
        }

        authenticateUser(loginRequest);
        String jwtToken = jwtService.createToken(user);
        activeSessions.put(jwtToken, user.getId()); // Store the new active session
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
        if (activeSessions.remove(token) != null) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("Logout successful.");
        }
        return ResponseEntity.badRequest().body("Invalid token. Logout failed.");
    }

    @Override
    public ResponseEntity<String> logoutAll(Long userId) {
        activeSessions.values().removeIf(sessionUserId -> sessionUserId.equals(userId));
        return ResponseEntity.ok("All active sessions have been terminated.");
    }
}
