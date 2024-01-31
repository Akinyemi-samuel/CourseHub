package com.coursehub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService = new JwtService();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);

        // Generate JWT token
        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

        // Return the token in the response
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("token", token);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        OutputStream out = response.getOutputStream();
        objectMapper.writeValue(out, successResponse);
        out.flush();
    }
}
