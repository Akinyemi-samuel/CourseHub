package com.coursehub.service;


import com.coursehub.exception.ApiException;
import com.coursehub.model.ConfirmationToken;
import com.coursehub.model.User;
import com.coursehub.repositories.ConfirmationTokenRepository;
import com.coursehub.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public String createConfirmationToken(User user){
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        confirmationTokenRepository.save(confirmationToken);
        return token;
    }


    @Transactional
    public String confirmUserEmailRegistration(String token){
         ConfirmationToken confirmationToken =
                 confirmationTokenRepository.findByToken(token)
                         .orElseThrow(()-> new RuntimeException("Verification Token does not exists"));

         if (confirmationToken.getConfirmedAt() != null){
             return "Token is already confirmed";
         }

         if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
             return "Token is already expired";
         }

         ConfirmUserEmail(confirmationToken.getUser());

        updateTokenConfirmedAt(confirmationToken.getToken());

         return "Your email Has been confirmed successfully";
    }


    @Transactional
    public void updateTokenConfirmedAt(String token){
        confirmationTokenRepository.updateConfirmedAt(token, String.valueOf(LocalDateTime.now()));
    }

    public User getUserByToken(String token){
        ConfirmationToken confirmationToken =
                confirmationTokenRepository.findByToken(token)
                        .orElseThrow(()-> new RuntimeException("Verification Token does not exists"));

        return  confirmationToken.getUser();
    }


    public void ConfirmUserEmail(User user) {

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (!userOptional.isPresent()) throw new ApiException("User Credentials Not Found", HttpStatus.NOT_FOUND);

        userRepository.updateEmailStatus(user.getEmail(), true);
    }
}
