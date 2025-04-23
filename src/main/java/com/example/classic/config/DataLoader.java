package com.example.classic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.classic.model.*;
import com.example.classic.repository.*;

import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                          CarRepository carRepository,
                          BrokerRepository brokerRepository,
                          OwnershipDetailsRepository ownershipDetailsRepository,
                          LoanDetailsRepository loanDetailsRepository) {
        return args -> {
            // Create test users
            Flux.just(
                User.builder()
                    .name("Admin User")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phoneNumber("1234567890")
                    .accessType("ADMIN")
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build(),
                User.builder()
                    .name("Test User")
                    .email("user@example.com")
                    .password(passwordEncoder.encode("user123"))
                    .phoneNumber("9876543210")
                    .accessType("USER")
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build()
            )
            .flatMap(userRepository::save)
            .thenMany(
                // Create test cars
                Flux.just(
                    Car.builder()
                        .userId(UUID.randomUUID())
                        .vin("ABC12345678901234")
                        .make("Toyota")
                        .model("Camry")
                        .year(2020)
                        .mileage(50000.0)
                        .mileageUnit("KM")
                        .color("Silver")
                        .registrationNumber("KA01AB1234")
                        .manufactureDate(OffsetDateTime.now().minusYears(2))
                        .purchaseDate(OffsetDateTime.now().minusMonths(6))
                        .sellerName("Local Dealer")
                        .listingPlatforms(List.of("OLX", "CarWale"))
                        .status(CarStatus.AVAILABLE)
                        .hasLoan(false)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build(),
                    Car.builder()
                        .userId(UUID.randomUUID())
                        .vin("DEF12345678901234")
                        .make("Honda")
                        .model("City")
                        .year(2021)
                        .mileage(30000.0)
                        .mileageUnit("KM")
                        .color("White")
                        .registrationNumber("KA02CD5678")
                        .manufactureDate(OffsetDateTime.now().minusYears(1))
                        .purchaseDate(OffsetDateTime.now().minusMonths(3))
                        .sellerName("Private Seller")
                        .listingPlatforms(List.of("OLX"))
                        .status(CarStatus.UNDER_MAINTENANCE)
                        .hasLoan(true)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build()
                )
                .flatMap(carRepository::save)
                .thenMany(
                    // Create test brokers
                    Flux.just(
                        Broker.builder()
                            .name("John Broker")
                            .phoneNumber("9876543210")
                            .commission(2.5)
                            .specialization(List.of(BrokerSpecialization.SALES, BrokerSpecialization.LOAN_PROCESSING))
                            .rating(4.5)
                            .totalDeals(10)
                            .createdAt(OffsetDateTime.now())
                            .updatedAt(OffsetDateTime.now())
                            .build(),
                        Broker.builder()
                            .name("Mike Dealer")
                            .phoneNumber("8765432109")
                            .commission(3.0)
                            .specialization(List.of(BrokerSpecialization.PURCHASE, BrokerSpecialization.INSURANCE))
                            .rating(4.8)
                            .totalDeals(15)
                            .createdAt(OffsetDateTime.now())
                            .updatedAt(OffsetDateTime.now())
                            .build()
                    )
                    .flatMap(brokerRepository::save)
                )
            )
            .subscribe();
        };
    }
} 