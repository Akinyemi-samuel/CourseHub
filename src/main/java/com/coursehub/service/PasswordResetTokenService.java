package com.coursehub.service;


import com.coursehub.exception.ApiException;
import com.coursehub.model.ConfirmationToken;
import com.coursehub.model.PasswordResetToken;
import com.coursehub.model.User;
import com.coursehub.repositories.PasswordResetTokenRepository;
import com.coursehub.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String createPasswordResetToken(User user){
        Random random = new Random();
        String token = String.valueOf(1000 + random.nextInt(9000));

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(60))
                .user(user)
                .build();

        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    @Transactional
    public String updatePasswordResetToken(User user){
        Random random = new Random();
        String token = String.valueOf(1000 + random.nextInt(9000));

       passwordResetTokenRepository.updatePassword(user.getUserId(), token, LocalDateTime.now().plusMinutes(60), LocalDateTime.now());
        return token;
    }

    public String ConfirmUserPasswordResetTokenByEmail(String email, String token) {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) throw new ApiException("User Credentials Not Found", HttpStatus.NOT_FOUND);
        User user = userOptional.get();

        Optional<PasswordResetToken> passwordResetTokenOptional =
                passwordResetTokenRepository.findByUser(user);


        if (passwordResetTokenOptional.isEmpty()){
            throw new ApiException("Invalid Token Code", HttpStatus.NOT_FOUND);
        }
        PasswordResetToken passwordResetToken =passwordResetTokenOptional.get();

        if (! Objects.equals(passwordResetToken.getToken(), token)){
            throw new ApiException("The Token Entered Is Not Valid", HttpStatus.NOT_FOUND);
        }

        if (passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ApiException("Token is already expired", HttpStatus.NOT_FOUND);
        }

        return "Token has been validated successfully";
    }



}
