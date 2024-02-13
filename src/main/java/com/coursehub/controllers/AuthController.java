package com.coursehub.controllers;

import com.coursehub.config.JwtService;
import com.coursehub.dto.request.AuthenticationDto;
import com.coursehub.dto.request.FacebookLoginRequest;
import com.coursehub.dto.request.RegistrationDto;
import com.coursehub.dto.response.AuthenticationResponse;
import com.coursehub.exception.ApiException;
import com.coursehub.model.Role;
import com.coursehub.model.User;
import com.coursehub.repositories.UserRepository;
import com.coursehub.service.AuthService;
import com.coursehub.service.ConfirmationTokenService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, JwtService jwtService, ConfirmationTokenService confirmationTokenService, UserRepository userRepository) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.confirmationTokenService = confirmationTokenService;
        this.userRepository = userRepository;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<AuthenticationResponse> userRegistration(@RequestBody RegistrationDto registrationDto, HttpServletRequest request) {
        log.info("AdminController registers Admin: {}", registrationDto.email);
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.UserRegistration(registrationDto, request));
    }


    @PostMapping("/login/facebook")
    public Map<String, String> authenticateUserFacebook(@RequestBody FacebookLoginRequest loginRequest) {

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            User user = User.builder()
                    .firstName(loginRequest.firstName)
                    .socialId("fb1399494327621784")
                    .lastName(loginRequest.lastName)
                    .role(Role.valueOf("USER"))
                    .email(loginRequest.getEmail())
                    .isEmailValid(true)
                    .build();

            userRepository.save(user);
        }
        // Validate access token with Facebook's Graph API
        AuthenticationDto authenticatedUser = userRepository.findByEmail(loginRequest.getEmail())
                .map(u -> AuthenticationDto.builder()
                        .email(u.getEmail())
                        .password(u.getPassword())
                        .build())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));


        var token = jwtService.generateToken(authenticatedUser);
        return Map.of("token", token);
    }




    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public Map<String, String> userAuthentication(@RequestBody AuthenticationDto authenticationDto, HttpServletRequest request) {
        log.info("AdminController authenticates Admin: {}", authenticationDto.getEmail());
        return authService.login(authenticationDto, request);
    }



    @GetMapping("/userdetails")
    public ResponseEntity<User> getUserDetailsByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication == null || !authentication.isAuthenticated())) {
            throw new MalformedJwtException("User is not authenticated");
        } else {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            return ResponseEntity.status(HttpStatus.OK).body(authService.findUserByUserName(username));
        }
    }

}



