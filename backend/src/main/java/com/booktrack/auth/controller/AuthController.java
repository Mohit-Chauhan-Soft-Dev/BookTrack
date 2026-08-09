package com.booktrack.auth.controller;

import com.booktrack.auth.dto.request.LoginRequest;
import com.booktrack.auth.dto.request.RefreshTokenRequest;
import com.booktrack.auth.dto.request.RegisterRequest;
import com.booktrack.auth.dto.response.JwtResponse;
import com.booktrack.auth.dto.response.RefreshTokenResponse;
import com.booktrack.auth.service.AuthenticationService;
import com.booktrack.common.constants.ApplicationConstants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        authenticationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authenticationService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                authenticationService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestParam String refreshToken) {

        authenticationService.logout(refreshToken);

        return ResponseEntity.ok("Logged out successfully.");
    }

}