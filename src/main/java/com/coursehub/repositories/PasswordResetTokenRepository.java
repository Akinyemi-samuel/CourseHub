package com.coursehub.repositories;

import com.coursehub.model.PasswordResetToken;
import com.coursehub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByUser(User user);
}