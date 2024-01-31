package com.coursehub.controllers;

import com.coursehub.dto.request.ForgotPasswordChange;
import com.coursehub.service.ConfirmationTokenService;
import com.coursehub.service.PasswordResetTokenService;
import com.coursehub.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final UserService userService;

    public UserController(ConfirmationTokenService confirmationTokenService, PasswordResetTokenService passwordResetTokenService, UserService userService) {
        this.confirmationTokenService = confirmationTokenService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.userService = userService;
    }


    @GetMapping("/passwordreset/{email}/{password}/p")
    public Map<String, String> UpdatePasswordOnPasswordForgot(@PathVariable String password, @PathVariable String email){
        return userService.updateUserPassword(email, password);
    }


    @GetMapping("/passwordreset/{email}/verify")
    public String passwordResetEmailValid(@PathVariable String email, HttpServletRequest request){
        return userService.checkIfUserExistsForOTPPasswordChange(email, request);
    }


    @GetMapping("/passwordreset/{email}/{token}")
    public String confirmRegistrationByToken(@PathVariable String email, @PathVariable String token){
        return passwordResetTokenService.ConfirmUserPasswordResetTokenByEmail(email, token);
    }

    @GetMapping("/REGISTRATION/confirm")
    public String confirmRegistrationByToken(@RequestParam(value="token")String token ){
        return confirmationTokenService.confirmUserEmailRegistration(token);
    }



}
