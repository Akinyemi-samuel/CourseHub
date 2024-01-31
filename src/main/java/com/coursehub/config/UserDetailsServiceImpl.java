package com.coursehub.config;

import com.coursehub.dto.request.AuthenticationDto;
import com.coursehub.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            return userRepository.findByEmail(username).map(u -> AuthenticationDto.builder()
                    .email(u.getEmail())
                    .password(u.getPassword())
                    .role(u.getRole())
                    .build()).orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid Credentials", e);
        }

    }
}
