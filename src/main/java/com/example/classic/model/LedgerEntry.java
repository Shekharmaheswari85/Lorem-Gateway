package com.example.classic.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Table("ledger_entries")
public class LedgerEntry extends BaseEntity {
    @Column("user_id")
    private UUID userId;

    @Column("action_type")
    private String actionType;

    @Column("entity_type")
    private String entityType;

    @Column("entity_id")
    private UUID entityId;

    @Column("details")
    private Map<String, Object> details;

    @Column("timestamp")
    private OffsetDateTime timestamp;

    @Column("ip_address")
    private String ipAddress;

    @Column("user_agent")
    private String userAgent;
} 