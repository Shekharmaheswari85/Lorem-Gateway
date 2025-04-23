package com.example.classic.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.classic.model.User;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends R2dbcRepository<User, UUID> {
    
    @Query("""
        SELECT id, name, email, phone_number, access_type, created_at, updated_at
        FROM users
        WHERE email = $1
    """)
    Mono<User> findByEmail(String email);

    @Query("""
        UPDATE users
        SET name = COALESCE($1, name),
            email = COALESCE($2, email),
            phone_number = COALESCE($3, phone_number),
            updated_at = CURRENT_TIMESTAMP
        WHERE id = $4
        RETURNING id, name, email, phone_number, access_type, created_at, updated_at
    """)
    Mono<User> updateUser(String name, String email, String phoneNumber, UUID id);

    @Query("""
        UPDATE users
        SET password_hash = $1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = $2
    """)
    Mono<Void> updatePassword(String passwordHash, UUID id);

    Mono<Boolean> existsByEmail(String email);
} 