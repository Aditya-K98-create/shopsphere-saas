package com.ecommerce.backend.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.dto.LoginRequest;
import com.ecommerce.backend.dto.LoginResponse;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.jwt.JwtService;
import com.ecommerce.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

   

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "message", "Email is already registered!"
                    ));
        }

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("CUSTOMER");
        }

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "userId", savedUser.getId()
        ));
    }

    

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Invalid Email or Password"
                    ));
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Invalid Email or Password"
                    ));
        }

        String token = jwtService.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

}