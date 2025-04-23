package com.example.classic.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.classic.model.Car;
import com.example.classic.model.CarStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CarRepository extends R2dbcRepository<Car, UUID> {
    
    @Query("""
        SELECT c.*,
          (SELECT TOP 1 JSON_QUERY((SELECT l.id, l.loan_amount, l.loan_status FOR JSON PATH, WITHOUT_ARRAY_WRAPPER))
           FROM loan_details l
           WHERE l.car_id = c.id AND l.loan_status = 'ACTIVE'
           ORDER BY l.created_at DESC) as active_loan
        FROM cars c
        WHERE c.id = $1
    """)
    Mono<Car> findByIdWithActiveLoan(UUID id);

    @Query("""
        SELECT c.*
        FROM cars c
        WHERE ($1 IS NULL OR c.status = $1)
        AND ($2 IS NULL OR c.user_id = $2)
        AND ($3 IS NULL OR c.name ILIKE $3 OR c.registration_number ILIKE $3)
        ORDER BY c.created_at DESC
        LIMIT $4 OFFSET $5
    """)
    Flux<Car> findAllWithFilters(CarStatus status, UUID ownerId, String search, int limit, int offset);

    @Query("""
        UPDATE cars
        SET status = $1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = $2
        RETURNING *
    """)
    Mono<Car> updateStatus(CarStatus status, UUID id);

    @Query("""
        SELECT COUNT(*) > 0
        FROM cars
        WHERE id = $1 AND user_id = $2
    """)
    Mono<Boolean> isOwner(UUID carId, UUID userId);

    @Query("""
        SELECT COUNT(*) > 0
        FROM loan_details
        WHERE car_id = $1 AND loan_status = 'ACTIVE'
    """)
    Mono<Boolean> hasActiveLoan(UUID carId);
} 