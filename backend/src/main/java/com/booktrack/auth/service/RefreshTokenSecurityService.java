package com.booktrack.auth.service;

public interface RefreshTokenSecurityService {

    void revokeTokenFamily(String tokenFamily);
}