package com.example.classic.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.classic.model.AuthPayload;
import com.example.classic.model.User;
import com.example.classic.repository.UserRepository;
import com.example.classic.security.JwtTokenProvider;

import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public Mono<AuthPayload> login(String email, String password) {
        log.debug("Authenticating user: {}", email);
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        
        return userRepository.findByEmail(email)
            .map(user -> {
                log.debug("User authenticated successfully: {}", email);
                return new AuthPayload(token, user);
            })
            .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
            .doOnError(error -> log.error("Authentication failed for user {}: {}", email, error.getMessage()));
    }

    public Mono<AuthPayload> register(String email, String password, String name) {
        log.debug("Registering new user: {}", email);
        return userRepository.existsByEmail(email)
            .flatMap(exists -> {
                if (exists) {
                    log.error("Registration failed: Email {} already exists", email);
                    return Mono.error(new RuntimeException("Email already exists"));
                }
                
                User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .accessType("USER")
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build();
                
                return userRepository.save(user)
                    .map(savedUser -> {
                        log.debug("User registered successfully: {}", email);
                        Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(email, password)
                        );
                        String token = jwtTokenProvider.generateToken(authentication);
                        return new AuthPayload(token, savedUser);
                    });
            })
            .doOnError(error -> log.error("Registration failed for user {}: {}", email, error.getMessage()));
    }

    public Mono<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.debug("Fetching current user: {}", email);
        return userRepository.findByEmail(email)
            .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
            .doOnSuccess(user -> log.debug("Successfully fetched user: {}", email))
            .doOnError(error -> log.error("Error fetching user {}: {}", email, error.getMessage()));
    }
} 