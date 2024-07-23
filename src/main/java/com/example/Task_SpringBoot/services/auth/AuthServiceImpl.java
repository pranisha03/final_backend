package com.example.Task_SpringBoot.services.auth;

import com.example.Task_SpringBoot.dto.SignupRequest;
import com.example.Task_SpringBoot.dto.UserDto;
import com.example.Task_SpringBoot.entities.User;
import com.example.Task_SpringBoot.enums.UserRole;
import com.example.Task_SpringBoot.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void createInitialAdminAccount() {
        // This method will no longer ensure that only one admin exists.
        // It is now just a placeholder if you want to create a default admin.
    }

    @Override
    public UserDto signupUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        // Use the role from the signup request, defaulting to EMPLOYEE if not provided
        UserRole userRole = signupRequest.getUserRole() != null ? signupRequest.getUserRole() : UserRole.EMPLOYEE;
        user.setUserRole(userRole);

        User createdUser = userRepository.save(user);
        return createdUser.getUserDto();
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
