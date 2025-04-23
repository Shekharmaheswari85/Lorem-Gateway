package com.example.classic.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.example.classic.model.*;
import com.example.classic.repository.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class QueryResolver {
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final BrokerRepository brokerRepository;
    private final TransactionRepository transactionRepository;
    private final OwnershipDetailsRepository ownershipDetailsRepository;
    private final LoanDetailsRepository loanDetailsRepository;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public Mono<User> me() {
        // TODO: Implement authentication and get current user
        return Mono.empty();
    }

    @QueryMapping
    public Mono<Car> car(@Argument UUID id) {
        return carRepository.findById(id);
    }

    @QueryMapping
    public Flux<Car> cars(
            @Argument CarStatus status,
            @Argument UUID ownerId,
            @Argument String search,
            @Argument Integer limit,
            @Argument Integer offset) {
        // TODO: Implement filtering and pagination
        return carRepository.findAll();
    }

    @QueryMapping
    public Mono<Broker> broker(@Argument UUID id) {
        return brokerRepository.findById(id);
    }

    @QueryMapping
    public Flux<Broker> brokers(
            @Argument BrokerSpecialization specialization,
            @Argument String search,
            @Argument Integer limit,
            @Argument Integer offset) {
        // TODO: Implement filtering and pagination
        return brokerRepository.findAll();
    }

    @QueryMapping
    public Mono<Transaction> transaction(@Argument UUID id) {
        return transactionRepository.findById(id);
    }

    @QueryMapping
    public Flux<Transaction> transactions(
            @Argument UUID carId,
            @Argument UUID brokerId,
            @Argument TransactionType type,
            @Argument TransactionStatus status,
            @Argument String fromDate,
            @Argument String toDate,
            @Argument Integer limit,
            @Argument Integer offset) {
        // TODO: Implement filtering and pagination
        return transactionRepository.findAll();
    }

    @QueryMapping
    public Flux<Transaction> carTransactions(@Argument UUID carId) {
        return transactionRepository.findByCarId(carId);
    }

    @QueryMapping
    public Flux<Transaction> brokerTransactions(@Argument UUID brokerId) {
        return transactionRepository.findByBrokerId(brokerId);
    }

    @QueryMapping
    public Flux<OwnershipDetails> carOwnershipHistory(@Argument UUID carId) {
        return ownershipDetailsRepository.findByCarId(carId);
    }

    @QueryMapping
    public Flux<LoanDetails> carLoanHistory(@Argument UUID carId) {
        return loanDetailsRepository.findByCarId(carId);
    }
} 