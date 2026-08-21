package com.booktrack.auth.repository;

import com.booktrack.auth.entity.RefreshToken;
import com.booktrack.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    @Modifying
    @Query("""
            UPDATE RefreshToken r
            SET r.revoked = true
            WHERE r.id = :id
              AND r.revoked = false
            """)
    int revokeIfActive(@Param("id") Long id);

    @Modifying
    @Query("""
            UPDATE RefreshToken r
            SET r.revoked = true
            WHERE r.tokenFamily = :tokenFamily
              AND r.revoked = false
            """)
    int revokeActiveFamily(
            @Param("tokenFamily") String tokenFamily);
}