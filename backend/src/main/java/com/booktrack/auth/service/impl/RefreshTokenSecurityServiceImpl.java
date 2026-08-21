package com.booktrack.auth.service.impl;

import com.booktrack.auth.repository.RefreshTokenRepository;
import com.booktrack.auth.service.RefreshTokenSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenSecurityServiceImpl
        implements RefreshTokenSecurityService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeTokenFamily(String tokenFamily) {

        refreshTokenRepository.revokeActiveFamily(tokenFamily);
    }
}