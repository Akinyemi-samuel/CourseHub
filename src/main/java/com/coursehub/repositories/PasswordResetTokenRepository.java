package com.coursehub.repositories;

import com.coursehub.model.PasswordResetToken;
import com.coursehub.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByUser(User user);

    @Transactional
    @Modifying
    @Query(
            value = "update password_reset SET token = ?2, expires_at = ?3, created_at = ?4 WHERE user = ?1",
            nativeQuery = true
    )
    void updatePassword (Long user, String token, LocalDateTime expires_at, LocalDateTime createdAt);
}