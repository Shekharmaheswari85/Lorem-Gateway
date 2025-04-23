package com.example.classic.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.classic.model.BrokerFee;
import com.example.classic.model.PaymentStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface BrokerFeeRepository extends R2dbcRepository<BrokerFee, UUID> {
    Flux<BrokerFee> findByBrokerId(UUID brokerId);
    Flux<BrokerFee> findByTransactionId(UUID transactionId);
    Mono<BrokerFee> findByBrokerIdAndTransactionId(UUID brokerId, UUID transactionId);
    Flux<BrokerFee> findByPaymentStatus(PaymentStatus status);
} 