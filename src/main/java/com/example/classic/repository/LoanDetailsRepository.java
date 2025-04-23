package com.example.classic.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.classic.model.LoanDetails;
import com.example.classic.model.LoanStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface LoanDetailsRepository extends R2dbcRepository<LoanDetails, UUID> {
    
    @Query("""
        SELECT l.*
        FROM loan_details l
        WHERE l.car_id = $1 AND l.user_id = $2
        ORDER BY l.created_at DESC
        LIMIT 1
    """)
    Mono<LoanDetails> findLatestByCarIdAndUserId(UUID carId, UUID userId);

    @Query("""
        SELECT l.*
        FROM loan_details l
        WHERE l.car_id = $1
        ORDER BY l.created_at DESC
    """)
    Flux<LoanDetails> findByCarId(UUID carId);

    @Query("""
        UPDATE loan_details
        SET loan_status = $1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = $2 AND user_id = $3
        RETURNING *
    """)
    Mono<LoanDetails> updateStatus(LoanStatus status, UUID id, UUID userId);

    @Query("""
        SELECT COUNT(*) > 0
        FROM loan_details
        WHERE car_id = $1 AND loan_status = 'ACTIVE'
    """)
    Mono<Boolean> hasActiveLoan(UUID carId);
} 