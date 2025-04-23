package com.example.classic.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.classic.model.Transaction;
import com.example.classic.model.TransactionStatus;
import com.example.classic.model.TransactionType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface TransactionRepository extends R2dbcRepository<Transaction, UUID> {
    
    @Query("""
        SELECT t.*, 
          json_build_object(
            'id', c.id,
            'name', c.name,
            'registrationNumber', c.registration_number
          ) as car
        FROM transactions t
        LEFT JOIN cars c ON t.car_id = c.id
        WHERE t.id = $1
    """)
    Mono<Transaction> findByIdWithCar(UUID id);

    @Query("""
        SELECT t.*
        FROM transactions t
        WHERE t.car_id = $1
        ORDER BY t.created_at DESC
    """)
    Flux<Transaction> findByCarId(UUID carId);

    @Query("""
        SELECT t.*
        FROM transactions t
        WHERE t.broker_id = $1
        ORDER BY t.created_at DESC
    """)
    Flux<Transaction> findByBrokerId(UUID brokerId);

    @Query("""
        SELECT t.*
        FROM transactions t
        WHERE ($1 IS NULL OR t.car_id = $1)
        AND ($2 IS NULL OR t.broker_id = $2)
        AND ($3 IS NULL OR t.transaction_type = $3)
        AND ($4 IS NULL OR t.status = $4)
        AND ($5 IS NULL OR t.created_at >= $5)
        AND ($6 IS NULL OR t.created_at <= $6)
        ORDER BY t.created_at DESC
        LIMIT $7 OFFSET $8
    """)
    Flux<Transaction> findAllWithFilters(
        UUID carId,
        UUID brokerId,
        TransactionType type,
        TransactionStatus status,
        OffsetDateTime fromDate,
        OffsetDateTime toDate,
        int limit,
        int offset
    );

    @Query("""
        UPDATE transactions
        SET status = $1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = $2
        RETURNING *
    """)
    Mono<Transaction> updateStatus(TransactionStatus status, UUID id);
} 