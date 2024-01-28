package com.coursehub.repositories;

import com.coursehub.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query(
            value = "update users SET is_email_valid = ?2 WHERE email = ?1",
            nativeQuery = true
    )
    void updateEmailStatus (String email, boolean is_email_valid);


    @Transactional
    @Modifying
    @Query(
            value = "update users SET password = ?2 WHERE email = ?1",
            nativeQuery = true
    )
    void updatePassword (String email, String password);

}