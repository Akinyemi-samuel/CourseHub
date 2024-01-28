package com.coursehub.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "password_reset")
public class PasswordResetToken {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "password_reset_token_generator"
    )
    @SequenceGenerator(
            sequenceName = "password_reset_token_generator",
            name = "password_reset_token_generator",
            allocationSize = 1,
            initialValue = 1
    )
    private Long id;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name = "User_id")
    private User user;
}
