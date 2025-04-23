package com.example.classic.resolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import com.example.classic.model.LedgerEntry;
import com.example.classic.service.LedgerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class LedgerResolver implements GraphQLQueryResolver, GraphQLMutationResolver {
    private final LedgerService ledgerService;

    public LedgerResolver(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    public Flux<LedgerEntry> ledgerEntries(UUID userId, String entityType, UUID entityId, String actionType,
                                         OffsetDateTime fromDate, OffsetDateTime toDate) {
        return ledgerService.getEntries(userId, entityType, entityId, actionType, fromDate, toDate);
    }

    public Mono<LedgerEntry> recordLedgerEntry(UUID userId, String actionType, String entityType, UUID entityId,
                                             Map<String, Object> details, String ipAddress, String userAgent) {
        return ledgerService.recordEntry(userId, actionType, entityType, entityId, details, ipAddress, userAgent);
    }
} 