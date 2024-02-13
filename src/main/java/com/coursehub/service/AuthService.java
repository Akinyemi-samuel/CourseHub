package com.coursehub.service;

import com.coursehub.config.JwtService;
import com.coursehub.dto.request.AuthenticationDto;
import com.coursehub.dto.request.BookingDto;
import com.coursehub.dto.request.RegistrationDto;
import com.coursehub.dto.response.AuthenticationResponse;
import com.coursehub.exception.ApiException;
import com.coursehub.model.Role;
import com.coursehub.model.User;
import com.coursehub.repositories.UserRepository;
import com.coursehub.validations.IsEmailValid;
import com.coursehub.validations.IsPasswordValid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final IsEmailValid isEmailValid;
    private final IsPasswordValid isPasswordValid;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    public AuthService(IsEmailValid isEmailValid, IsPasswordValid isPasswordValid, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, EmailService emailService, ConfirmationTokenService confirmationTokenService) {
        this.isEmailValid = isEmailValid;
        this.isPasswordValid = isPasswordValid;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
    }

    public User findUserByUserName(String userName) {
        Optional<User> optional = userRepository.findByEmail(userName);
        return optional.orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    public AuthenticationResponse UserRegistration(RegistrationDto registrationDto, HttpServletRequest request) {

        if (!isEmailValid.test(registrationDto.email))
            throw new ApiException("Invalid Email Found", HttpStatus.NOT_ACCEPTABLE);

        if (!isPasswordValid.test(registrationDto.password))
            throw new ApiException("Password is Invalid", HttpStatus.NOT_ACCEPTABLE);

        Optional<User> userOptional = userRepository.findByEmail(registrationDto.email);
        if (userOptional.isPresent()) {
            throw new ApiException("Email Already Exists", HttpStatus.NOT_FOUND);
        }

        User user = User.builder()
                .firstName(registrationDto.firstName)
                .socialId(registrationDto.socialId)
                .lastName(registrationDto.lastName)
                .role(Role.valueOf("USER"))
                .email(registrationDto.email)
                .password(passwordEncoder.encode(registrationDto.password))
                .build();

        userRepository.save(user);
        String token = confirmationTokenService.createConfirmationToken(user);
        String url = applicationUrl(request) + "/user/REGISTRATION/confirm?token=" + token;
        emailService.send(user.getEmail(), emailService.confirmRegistrationBuildEmail(user.getLastName(), url));

        return AuthenticationResponse
                .builder()
                .token("Registration successful!")
                .build();
    }


    public Map<String, String> login(AuthenticationDto authenticationDto, HttpServletRequest request) {

        User user = userRepository.findByEmail(authenticationDto.getEmail()).orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        if (!user.isEmailValid()) {
            String token = confirmationTokenService.createConfirmationToken(user);
            String url = applicationUrl(request) + "/user/REGISTRATION/confirm?token=" + token;
            emailService.send(user.getEmail(), emailService.confirmRegistrationBuildEmail(user.getLastName(), url));
            throw new ApiException("Your email address has not been verified, please go to your email address to verify", HttpStatus.UNAUTHORIZED);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationDto.getEmail(),
                            authenticationDto.getPassword()
                    )
            );

            // Check if authentication is successful
            if (authentication.isAuthenticated()) {
                // if user is authenticated, return a json web token

                AuthenticationDto authenticatedUser = userRepository.findByEmail(authenticationDto.getEmail())
                        .map(u -> AuthenticationDto.builder()
                                .email(u.getEmail())
                                .password(u.getPassword())
                                .build())
                        .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));


                var token = jwtService.generateToken(authenticatedUser);
                return Map.of("token", token);
            } else {
                throw new ApiException("Invalid Credentials. Please check your email or password.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            // Handle other authentication-related exceptions if needed
            throw new ApiException("Invalid Credentials. Please check your email or password.", HttpStatus.UNAUTHORIZED);
        }
    }


    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
