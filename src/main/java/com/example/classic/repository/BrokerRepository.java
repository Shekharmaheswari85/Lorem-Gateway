package com.example.classic.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.classic.model.Broker;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface BrokerRepository extends R2dbcRepository<Broker, UUID> {
    
    @Query("""
        SELECT b.*,
          (SELECT COUNT(*) FROM transactions t 
           WHERE t.broker_id = b.id AND t.status = 'COMPLETED') as completed_deals,
          (SELECT COALESCE(SUM(bf.fee_amount), 0) FROM broker_fees bf 
           WHERE bf.broker_id = b.id AND bf.payment_status = 'COMPLETED') as total_earnings
        FROM brokers b
        WHERE b.id = $1
    """)
    Mono<Broker> findByIdWithMetrics(UUID id);

    @Query("""
        SELECT b.*,
          (SELECT COUNT(*) FROM transactions t 
           WHERE t.broker_id = b.id AND t.status = 'COMPLETED') as completed_deals,
          (SELECT COALESCE(SUM(bf.fee_amount), 0) FROM broker_fees bf 
           WHERE bf.broker_id = b.id AND bf.payment_status = 'COMPLETED') as total_earnings
        FROM brokers b
        WHERE ($1 IS NULL OR $1 = ANY(b.specialization))
        AND ($2 IS NULL OR b.name ILIKE $2 OR b.phone_number ILIKE $2)
        ORDER BY b.rating DESC, b.total_deals DESC
        LIMIT $3 OFFSET $4
    """)
    Flux<Broker> findAllWithMetrics(String specialization, String search, int limit, int offset);

    @Query("""
        UPDATE brokers
        SET rating = $1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = $2
        RETURNING *
    """)
    Mono<Broker> updateRating(Double rating, UUID id);
} 