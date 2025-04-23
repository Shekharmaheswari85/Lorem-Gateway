package com.example.classic.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Table("brokers")
public class Broker {
    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("phone_number")
    private String phoneNumber;

    @Column("commission")
    private Double commission;

    @Column("specialization")
    private List<BrokerSpecialization> specialization;

    @Column("rating")
    private Double rating;

    @Column("total_deals")
    private Integer totalDeals;

    @Column("completed_deals")
    private Integer completedDeals;

    @Column("total_earnings")
    private Double totalEarnings;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;
} 