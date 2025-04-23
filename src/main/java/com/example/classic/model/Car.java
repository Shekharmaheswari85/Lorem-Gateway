package com.example.classic.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@Table("cars")
public class Car {
    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("vin")
    private String vin;

    @Column("make")
    private String make;

    @Column("model")
    private String model;

    @Column("year")
    private Integer year;

    @Column("mileage")
    private Double mileage;

    @Column("mileage_unit")
    private String mileageUnit;

    @Column("color")
    private String color;

    @Column("name")
    private String name;

    @Column("registration_number")
    private String registrationNumber;

    @Column("manufacture_date")
    private OffsetDateTime manufactureDate;

    @Column("purchase_date")
    private OffsetDateTime purchaseDate;

    @Column("seller_name")
    private String sellerName;

    @Column("listing_platforms")
    private List<String> listingPlatforms;

    @Column("listing_links")
    private Map<String, String> listingLinks;

    @Column("listing_dates")
    private Map<String, OffsetDateTime> listingDates;

    @Column("documents")
    private Map<String, Object> documents;

    @Column("status")
    private CarStatus status;

    @Column("has_loan")
    private Boolean hasLoan;

    @Column("loan_details")
    private Map<String, Object> loanDetails;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_at")
    private OffsetDateTime updatedAt;
} 