package com.example.classic.service;

import org.springframework.stereotype.Service;

import com.example.classic.model.LedgerEntry;
import com.example.classic.repository.LedgerEntryRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class LedgerService {
    private final LedgerEntryRepository ledgerEntryRepository;

    public LedgerService(LedgerEntryRepository ledgerEntryRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    public Mono<LedgerEntry> recordEntry(UUID userId, String actionType, String entityType, UUID entityId, 
                                       Map<String, Object> details, String ipAddress, String userAgent) {
        LedgerEntry entry = new LedgerEntry();
        entry.setUserId(userId);
        entry.setActionType(actionType);
        entry.setEntityType(entityType);
        entry.setEntityId(entityId);
        entry.setDetails(details);
        entry.setTimestamp(OffsetDateTime.now());
        entry.setIpAddress(ipAddress);
        entry.setUserAgent(userAgent);
        
        return ledgerEntryRepository.save(entry);
    }

    public Flux<LedgerEntry> getEntries(UUID userId, String entityType, UUID entityId, String actionType,
                                      OffsetDateTime fromDate, OffsetDateTime toDate) {
        if (userId != null && fromDate != null && toDate != null) {
            return ledgerEntryRepository.findByUserIdAndTimestampBetween(userId, fromDate, toDate);
        } else if (userId != null) {
            return ledgerEntryRepository.findByUserId(userId);
        } else if (entityType != null) {
            return ledgerEntryRepository.findByEntityType(entityType);
        } else if (entityId != null) {
            return ledgerEntryRepository.findByEntityId(entityId);
        } else if (actionType != null) {
            return ledgerEntryRepository.findByActionType(actionType);
        } else if (fromDate != null && toDate != null) {
            return ledgerEntryRepository.findByTimestampBetween(fromDate, toDate);
        }
        return ledgerEntryRepository.findAll();
    }
} 