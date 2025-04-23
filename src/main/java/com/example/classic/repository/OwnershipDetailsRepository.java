package com.example.classic.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.classic.model.OwnershipDetails;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OwnershipDetailsRepository extends R2dbcRepository<OwnershipDetails, UUID> {
    Flux<OwnershipDetails> findByCarId(UUID carId);
    Mono<OwnershipDetails> findByCarIdAndIsCurrentOwner(UUID carId, Boolean isCurrentOwner);
} 