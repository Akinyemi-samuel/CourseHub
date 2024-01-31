package com.coursehub.repositories;

import com.coursehub.model.ConfirmationToken;
import com.coursehub.model.PasswordResetToken;
import com.coursehub.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {


    Optional<ConfirmationToken> findByUser(User user);
    Optional<ConfirmationToken> findByToken(String token);

    @Modifying
    @Query(
            value = "update confirmation_token SET confirmed_at = ?2 WHERE token = ?1",
            nativeQuery = true
    )
    void updateConfirmedAt(String token, String confirmedAt);

    @Transactional
    @Modifying
    @Query(
            value = "update confirmation_token SET token = ?2, expires_at = ?3, created_at = ?4 WHERE user = ?1",
            nativeQuery = true
    )
    void updateConfirmationToken (Long user, String token, LocalDateTime expires_at, LocalDateTime createdAt);

}