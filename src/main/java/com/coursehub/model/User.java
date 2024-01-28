package com.coursehub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_userid", columnList = "userId")
}, uniqueConstraints = {
        @UniqueConstraint(name = "email", columnNames = {"email"}),
        @UniqueConstraint(name = "social_id", columnNames = {"social_id"})
})
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_generator"
    )
    @SequenceGenerator(
            name = "user_generator",
            sequenceName = "user_generator",
            allocationSize = 1
    )
    @Getter
    private Long userId;

    @Column(unique = true, name = "social_id")
    @Getter
    private String socialId;


    @Column(name = "first_name", nullable = false)
    @Getter
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Getter
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Getter
    private String email;

    @Getter
    private String password;

    private boolean isEmailValid;

    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ConfirmationToken confirmationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PasswordResetToken passwordResetToken;

}
