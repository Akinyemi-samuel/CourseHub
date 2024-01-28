package com.coursehub.service;

import com.coursehub.dto.request.ChangePasswordRequest;
import com.coursehub.dto.request.ForgotPasswordChange;
import com.coursehub.exception.ApiException;
import com.coursehub.model.PasswordResetToken;
import com.coursehub.model.User;
import com.coursehub.repositories.UserRepository;
import com.coursehub.validations.IsPasswordValid;
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
    private final PasswordResetTokenService passwordResetTokenService;

    public UserService(IsPasswordValid isPasswordValid, UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, PasswordResetTokenService passwordResetTokenService) {
        this.isPasswordValid = isPasswordValid;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.passwordResetTokenService = passwordResetTokenService;
    }



    public String checkIfUserExistsPasswordChange(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = passwordResetTokenService.createPasswordResetToken(user);
            emailService.send(email, emailService.passwordResetTokenBuildemail(user.getLastName(), token));
        }else {
            throw new ApiException("User not found", HttpStatus.NOT_FOUND);
        }
        return "Please check your email address for further instructions";
    }


    public Map<String, String> updateUserPassword(String email, String password){
        userRepository.updatePassword(email, password);
        return Map.of("reponse", "Password updated successfully");
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
