package com.booktrack.auth.service;

import com.booktrack.auth.dto.request.LoginRequest;
import com.booktrack.auth.dto.request.RefreshTokenRequest;
import com.booktrack.auth.dto.request.RegisterRequest;
import com.booktrack.auth.dto.response.JwtResponse;
import com.booktrack.auth.dto.response.RefreshTokenResponse;

public interface AuthenticationService {

    void register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void logout(String refreshToken);
}