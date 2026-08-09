package com.booktrack.auth.service.impl;

import com.booktrack.auth.dto.request.LoginRequest;
import com.booktrack.auth.dto.request.RefreshTokenRequest;
import com.booktrack.auth.dto.request.RegisterRequest;
import com.booktrack.auth.dto.response.JwtResponse;
import com.booktrack.auth.dto.response.RefreshTokenResponse;
import com.booktrack.auth.repository.RefreshTokenRepository;
import com.booktrack.auth.service.AuthenticationService;
import com.booktrack.exception.DuplicateResourceException;
import com.booktrack.exception.ResourceNotFoundException;
import com.booktrack.role.repository.RoleRepository;
import com.booktrack.security.jwt.JwtService;
import com.booktrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.booktrack.role.entity.Role;
import com.booktrack.role.enums.RoleName;
import com.booktrack.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import com.booktrack.auth.entity.RefreshToken;
import com.booktrack.security.userdetails.CustomUserDetails;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.booktrack.security.jwt.JwtProperties;

import com.booktrack.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered.");
        }

        Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new IllegalStateException("ROLE_STUDENT is missing from database."));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .roles(Set.of(studentRole))
                .build();

        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        String accessToken = jwtService.generateAccessToken(userDetails);

        String refreshTokenValue = jwtService.generateRefreshToken(userDetails);

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .expiryDate(LocalDateTime.now().plusSeconds(jwtProperties.refreshTokenExpiration() / 1000))
                .user(user)
                .build();

        refreshTokenRepository.save(refreshToken);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .expiresIn(900L)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found."));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new BadRequestException("Refresh token has expired.");
        }

        CustomUserDetails userDetails = new CustomUserDetails(refreshToken.getUser());

        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public void logout(String refreshToken) {

        RefreshToken token = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found."));

        refreshTokenRepository.delete(token);
    }
}