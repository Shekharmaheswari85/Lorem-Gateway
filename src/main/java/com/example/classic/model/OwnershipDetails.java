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
@Table("ownership_details")
public class OwnershipDetails extends BaseEntity {
    @Column("car_id")
    private UUID carId;

    @Column("owner_name")
    private String ownerName;

    @Column("aadhaar_number")
    private String aadhaarNumber;

    @Column("pan_number")
    private String panNumber;

    @Column("driving_license")
    private String drivingLicense;

    @Column("phone_number")
    private String phoneNumber;

    @Column("location")
    private String location;

    @Column("documents")
    private Map<String, Object> documents;

    @Column("is_current_owner")
    private Boolean isCurrentOwner;

    @Column("ownership_start_date")
    private OffsetDateTime ownershipStartDate;

    @Column("ownership_end_date")
    private OffsetDateTime ownershipEndDate;
} 