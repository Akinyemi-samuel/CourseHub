package com.coursehub.service;

import com.coursehub.dto.request.FacebookLoginRequest;
import com.coursehub.exception.ApiException;
import com.coursehub.model.PasswordResetToken;
import com.coursehub.model.User;
import com.coursehub.repositories.PasswordResetTokenRepository;
import com.coursehub.repositories.UserRepository;
import com.coursehub.validations.IsPasswordValid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final IsPasswordValid isPasswordValid;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final ConfirmationTokenService confirmationTokenService;

    public UserService(IsPasswordValid isPasswordValid, UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, PasswordResetTokenRepository passwordResetTokenRepository, PasswordResetTokenService passwordResetTokenService, ConfirmationTokenService confirmationTokenService) {
        this.isPasswordValid = isPasswordValid;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordResetTokenService = passwordResetTokenService;
        this.confirmationTokenService = confirmationTokenService;
    }


    public FacebookLoginRequest findUserById(Long userId){

        FacebookLoginRequest facebookLoginRequest = userRepository.findById(userId)
                .map(n -> FacebookLoginRequest.builder()
                     .firstName(n.getFirstName())
                     .lastName(n.getLastName())
                     .email(n.getEmail())
                     .build()).orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        return facebookLoginRequest;
    }


    public String checkIfUserExistsForOTPPasswordChange(String email, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // check if the email provided is already verified
            if (!user.isEmailValid()){
                String token = confirmationTokenService.createConfirmationToken(user);
                String url = applicationUrl(request) + "/user/REGISTRATION/confirm?token=" + token;
                emailService.send(user.getEmail(), emailService.confirmRegistrationBuildEmail(user.getLastName(), url));
                throw new ApiException("Your email address has not been verified, please go to your email address to verify", HttpStatus.UNAUTHORIZED);
            }

            //check if the user has already initiated a change for password
            Optional<PasswordResetToken> passwordResetTokenOptional =
                    passwordResetTokenRepository.findByUser(user);

            if (passwordResetTokenOptional.isPresent()) {
                String token = passwordResetTokenService.updatePasswordResetToken(user);
                emailService.send(email, emailService.passwordResetTokenBuildemail(user.getLastName(), token));
            } else {
                String token = passwordResetTokenService.createPasswordResetToken(user);
                emailService.send(email, emailService.passwordResetTokenBuildemail(user.getLastName(), token));
            }

        } else {
            throw new ApiException("User not found", HttpStatus.NOT_FOUND);
        }
        return "Please check your email address for further instructions";
    }


    public Map<String, String> updateUserPassword(String email, String password) {

        userRepository.updatePassword(email, passwordEncoder.encode(password));
        return Map.of("reponse", "Password updated successfully");
    }




    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
       /* public ResponseEntity<String> forgotPassword(ChangePasswordRequest changePasswordRequest){


        // Authenticate the user with the provided old password
        String username = authentication.getName();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, changePasswordRequest.getOldPassword());
        authenticationManager.authenticate(authenticationToken);

        // If authentication successful, update the password
        if (authenticationToken.isAuthenticated()) {
            // Fetch user by username
            Admin admin = findUserByUserName(username);

            if (admin != null) {
                String newPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
                admin.setPassword(newPassword);

                // Save the updated user with the new password
                adminRepository.save(admin);
                return ResponseEntity.ok("Password updated successfully");
            } else {
                return ResponseEntity.badRequest().body("User not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

    } **/

}
