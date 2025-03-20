package com.example.graphqldemo.config;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Component
public class MDCWebFilter implements WebFilter {
    private static final String TRACE_ID = "traceId";
    private static final String CORRELATION_ID = "correlationId";

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String traceId = UUID.randomUUID().toString();
        String correlationId = exchange.getRequest().getHeaders()
                .getFirst("X-Correlation-ID");
        
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        Map<String, String> contextMap = Map.of(
            TRACE_ID, traceId,
            CORRELATION_ID, correlationId
        );

        return chain.filter(exchange)
                .contextWrite(MDCContextLifter.withContext(contextMap));
    }
} 