package com.james.vendingmachine.service.serviceImp;

import com.james.vendingmachine.dto.UserRequest;
import com.james.vendingmachine.dto.UserResponse;
import com.james.vendingmachine.exceptionHandler.customException.UserAlreadyExistException;
import com.james.vendingmachine.exceptionHandler.customException.UserNotFoundException;
import com.james.vendingmachine.model.User;
import com.james.vendingmachine.repository.UserRepository; // Ensure to import your EmailService
import com.james.vendingmachine.service.UserService;
import com.james.vendingmachine.service.serviceImp.Notification.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NotificationService emailService;

    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest userRequest) throws MessagingException {
        validateUserRequest(userRequest);
        User user = modelMapper.map(userRequest, User.class);
        userRepository.save(user);
        emailService.sendRegistrationNotification(user.getFirstName(), user.getUsername());

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setMessage("Registration successful"); // Set the success message

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }


    private void validateUserRequest(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new UserAlreadyExistException("User with this username has already been registered");
        }
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
}
