package com.example.classic.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Table("transactions")
public class Transaction {
    @Id
    private UUID id;

    @Column("car_id")
    private UUID carId;

    @Column("broker_id")
    private UUID brokerId;

    @Column("transaction_type")
    private TransactionType transactionType;

    @Column("payment_method")
    private PaymentMethod paymentMethod;

    @Column("amount")
    private BigDecimal amount;

    @Column("transaction_id")
    private String transactionId;

    @Column("bank_details")
    private Map<String, Object> bankDetails;

    @Column("notes")
    private String notes;

    @Column("status")
    private TransactionStatus status;

    @Column("documents")
    private List<String> documents;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;
} 