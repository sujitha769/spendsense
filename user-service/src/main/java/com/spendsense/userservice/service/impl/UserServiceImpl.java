package com.spendsense.userservice.service.impl;

import com.spendsense.userservice.dto.UserRegistrationRequest;
import com.spendsense.userservice.dto.UserResponse;
import com.spendsense.userservice.entity.User;
import com.spendsense.userservice.repository.UserRepository;
import com.spendsense.userservice.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserResponse registerUser(
            UserRegistrationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        // Never store the plain-text password
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser =
                userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found with id: " + id
                                ));

        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {

        UserResponse response =
                new UserResponse();

        response.setId(user.getId());

        response.setName(user.getName());

        response.setEmail(user.getEmail());

        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
}