package com.example.classic.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Table("broker_fees")
public class BrokerFee extends BaseEntity {
    @Column("broker_id")
    private UUID brokerId;

    @Column("transaction_id")
    private UUID transactionId;

    @Column("fee_amount")
    private BigDecimal feeAmount;

    @Column("payment_status")
    private PaymentStatus paymentStatus;

    @Column("payment_date")
    private OffsetDateTime paymentDate;
} 