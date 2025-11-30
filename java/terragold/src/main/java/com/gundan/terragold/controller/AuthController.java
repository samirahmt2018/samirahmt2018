package com.gundan.terragold.controller;

import com.gundan.terragold.dto.request.LoginRequest;
import com.gundan.terragold.entity.AppUser;
import com.gundan.terragold.repository.UserRepository;
import com.gundan.terragold.util.ApiResponseBuilder;
import com.gundan.terragold.util.JwtService; // <-- Import JwtService
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final ApiResponseBuilder apiResponseBuilder;
    private final JwtService jwtService; // <-- Inject JwtService

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        AppUser user = userRepo.findByUsername(req.username()).orElse(null);

        // --- Authentication Failure ---
        if (user == null || !passwordEncoder.matches(req.password(), user.getPassword())) {

            List<List<Map<String, String>>> errorMessage = List.of(
                    List.of(
                            Map.of("message", "Invalid username or password")
                    )
            );

            return apiResponseBuilder.buildResponse(
                    null,
                    null,
                    null,
                    null,
                    errorMessage,
                    null,
                    HttpStatus.UNAUTHORIZED
            );
        }

        // --- Authentication Success ---
        // 1. Generate the JWT token
        String jwtToken = jwtService.generateToken(user.getUsername());

        // 2. Create the response body including the token and user details
        Map<String, Object> responseData = Map.of(
                "user", user,
                "token", jwtToken,
                "token_type", "Bearer" // <-- Important: The standard token type
        );

        // 3. Return the success response
        return apiResponseBuilder.buildResponse(
                responseData, // Pass the Map containing user and token
                null,
                null,
                null,
                null,
                null,
                HttpStatus.OK
        );
    }
}