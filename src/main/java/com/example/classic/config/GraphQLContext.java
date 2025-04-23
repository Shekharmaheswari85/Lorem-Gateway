package com.example.classic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
public class GraphQLContext {
    private static final Logger log = LoggerFactory.getLogger(GraphQLContext.class);

    @QueryMapping
    public Mono<Map<String, String>> getContext() {
        return Mono.deferContextual(contextView -> {
            String correlationId = contextView.getOrDefault(MDCContextLifter.CORRELATION_ID, "unknown");
            log.debug("Retrieving context with correlationId: {}", correlationId);
            return Mono.just(Map.of("correlationId", correlationId));
        });
    }
} 