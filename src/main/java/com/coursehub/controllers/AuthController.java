package com.coursehub.controllers;

import com.coursehub.dto.request.AuthenticationDto;
import com.coursehub.dto.request.RegistrationDto;
import com.coursehub.model.User;
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

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final ConfirmationTokenService confirmationTokenService;

    public AuthController(AuthService authService, ConfirmationTokenService confirmationTokenService) {
        this.authService = authService;
        this.confirmationTokenService = confirmationTokenService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Map<String, String> userRegistration(@RequestBody RegistrationDto registrationDto, HttpServletRequest request) {
        log.info("AdminController registers Admin: {}", registrationDto.email);
        return authService.UserRegistration(registrationDto, request);
    }



    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public Map<String, String> userAuthentication(@RequestBody AuthenticationDto authenticationDto) {
        log.info("AdminController authenticates Admin: {}", authenticationDto.getEmail());
        return authService.login(authenticationDto);
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



