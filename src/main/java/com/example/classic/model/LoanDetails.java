package com.example.classic.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Table("loan_details")
public class LoanDetails {
    @Id
    private UUID id;

    @Column("car_id")
    private UUID carId;

    @Column("user_id")
    private UUID userId;

    @Column("bank_name")
    private String bankName;

    @Column("loan_amount")
    private BigDecimal loanAmount;

    @Column("emi_amount")
    private BigDecimal emiAmount;

    @Column("tenure")
    private Integer tenure;

    @Column("interest_rate")
    private BigDecimal interestRate;

    @Column("start_date")
    private OffsetDateTime startDate;

    @Column("end_date")
    private OffsetDateTime endDate;

    @Column("loan_status")
    private LoanStatus loanStatus;

    @Column("documents")
    private Map<String, Object> documents;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;
} 