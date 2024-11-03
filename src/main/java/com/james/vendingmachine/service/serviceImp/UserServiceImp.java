package com.james.vendingmachine.service.serviceImp;

import com.james.vendingmachine.dto.UserRequest;
import com.james.vendingmachine.dto.UserResponse;
import com.james.vendingmachine.exceptionHandler.customException.UserAlreadyExistException;
import com.james.vendingmachine.exceptionHandler.customException.UserNotFoundException;
import com.james.vendingmachine.model.User;
import com.james.vendingmachine.repository.UserRepository;
import com.james.vendingmachine.service.UserService;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new UserAlreadyExistException("User with this username has already been registered");
        }

        User user = modelMapper.map(userRequest, User.class);
        userRepository.save(user);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        UserResponse response = modelMapper.map(user, UserResponse.class);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        modelMapper.map(userRequest, user);
        User updatedUser = userRepository.save(user);
        UserResponse response = modelMapper.map(updatedUser, UserResponse.class);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deposit(Long userId, Integer amount) {
        List<Integer> validNotes = Arrays.asList(50, 100, 200, 500, 1000);
        if (!validNotes.contains(amount)) {
            return ResponseEntity.badRequest().body("Invalid note value. Valid values are: 50, 100, 200, 500, 1000.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return ResponseEntity.ok("Deposit successful. New balance: " + user.getBalance());
    }


    @Override
    public void resetUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setBalance(0);
        userRepository.save(user);
    }

}
