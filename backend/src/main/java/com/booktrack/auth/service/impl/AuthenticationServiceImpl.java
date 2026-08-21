package com.booktrack.auth.service.impl;

import com.booktrack.auth.dto.request.LoginRequest;
import com.booktrack.auth.dto.request.RefreshTokenRequest;
import com.booktrack.auth.dto.request.RegisterRequest;
import com.booktrack.auth.dto.response.JwtResponse;
import com.booktrack.auth.dto.response.RefreshTokenResponse;
import com.booktrack.auth.repository.RefreshTokenRepository;
import com.booktrack.auth.service.AuthenticationService;
import com.booktrack.auth.service.RefreshTokenSecurityService;
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
import java.util.UUID;

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
    private final RefreshTokenSecurityService refreshTokenSecurityService;

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

        String tokenFamily = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                        .token(refreshTokenValue)
                        .expiryDate(LocalDateTime.now().plusSeconds(
                                        jwtProperties.refreshTokenExpiration() / 1000))
                        .revoked(false)
                        .tokenFamily(tokenFamily)
                        .user(user)
                        .build();


        refreshTokenRepository.save(refreshToken);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .expiresIn(jwtProperties.accessTokenExpiration() / 1000)
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

            RefreshToken currentToken = refreshTokenRepository
                            .findByToken(request.getRefreshToken())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                            "Refresh token not found."));

            if (currentToken.isRevoked()) {

                    refreshTokenSecurityService.revokeTokenFamily(
                                    currentToken.getTokenFamily());

                    throw new BadRequestException(
                                    "Refresh token has already been used.");
            }

            if (currentToken.getExpiryDate()
                            .isBefore(LocalDateTime.now())) {

                    throw new BadRequestException(
                                    "Refresh token has expired.");
            }

            User user = currentToken.getUser();

            if (!user.isEnabled()
                            || !user.isAccountNonLocked()
                            || !user.isAccountNonExpired()
                            || !user.isCredentialsNonExpired()) {

                    throw new BadRequestException(
                                    "User account is not available.");
            }

            int revokedRows = refreshTokenRepository.revokeIfActive(
                            currentToken.getId());

            if (revokedRows != 1) {

                    refreshTokenRepository.revokeActiveFamily(
                                    currentToken.getTokenFamily());

                    throw new BadRequestException(
                                    "Refresh token has already been used.");
            }

            CustomUserDetails userDetails = new CustomUserDetails(user);

            String newAccessToken = jwtService.generateAccessToken(userDetails);

            String newRefreshToken = jwtService.generateRefreshToken(userDetails);

            String tokenFamily = currentToken.getTokenFamily();

            RefreshToken rotatedToken = RefreshToken.builder()
                            .token(newRefreshToken)
                            .expiryDate(
                                            LocalDateTime.now().plusSeconds(
                                                            jwtProperties
                                                                            .refreshTokenExpiration()
                                                                            / 1000))
                            .revoked(false)
                            .tokenFamily(tokenFamily)
                            .user(user)
                            .build();

            refreshTokenRepository.save(rotatedToken);

            return RefreshTokenResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken)
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