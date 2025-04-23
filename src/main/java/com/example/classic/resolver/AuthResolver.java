package com.example.classic.resolver;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.classic.model.AuthPayload;
import com.example.classic.model.User;
import com.example.classic.service.AuthService;

import reactor.core.publisher.Mono;

@Component
public class AuthResolver implements GraphQLQueryResolver, GraphQLMutationResolver {
    private static final Logger log = LoggerFactory.getLogger(AuthResolver.class);
    private final AuthService authService;

    public AuthResolver(AuthService authService) {
        this.authService = authService;
    }

    public Mono<User> me() {
        log.debug("Fetching current user details");
        return authService.getCurrentUser()
            .doOnSuccess(user -> log.debug("Successfully fetched user: {}", user.getEmail()))
            .doOnError(error -> log.error("Error fetching current user: {}", error.getMessage()));
    }

    public Mono<AuthPayload> login(String email, String password) {
        log.debug("Attempting login for user: {}", email);
        return authService.login(email, password)
            .doOnSuccess(auth -> log.debug("Login successful for user: {}", email))
            .doOnError(error -> log.error("Login failed for user {}: {}", email, error.getMessage()));
    }

    public Mono<AuthPayload> register(String email, String password, String name) {
        log.debug("Attempting registration for user: {}", email);
        return authService.register(email, password, name)
            .doOnSuccess(auth -> log.debug("Registration successful for user: {}", email))
            .doOnError(error -> log.error("Registration failed for user {}: {}", email, error.getMessage()));
    }
} 