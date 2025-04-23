package com.example.classic.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.classic.model.LedgerEntry;

import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface LedgerEntryRepository extends R2dbcRepository<LedgerEntry, UUID> {
    Flux<LedgerEntry> findByUserId(UUID userId);
    Flux<LedgerEntry> findByEntityType(String entityType);
    Flux<LedgerEntry> findByEntityId(UUID entityId);
    Flux<LedgerEntry> findByActionType(String actionType);
    Flux<LedgerEntry> findByTimestampBetween(OffsetDateTime startDate, OffsetDateTime endDate);
    Flux<LedgerEntry> findByUserIdAndTimestampBetween(UUID userId, OffsetDateTime startDate, OffsetDateTime endDate);
} 