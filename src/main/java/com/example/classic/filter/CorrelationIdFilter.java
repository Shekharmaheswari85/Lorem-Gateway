package com.example.classic.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.example.classic.config.MDCContextLifter;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String correlationId = exchange.getRequest().getHeaders().getFirst(MDCContextLifter.CORRELATION_ID) != null ?
                exchange.getRequest().getHeaders().getFirst(MDCContextLifter.CORRELATION_ID) :
                UUID.randomUUID().toString();

        log.debug("Processing request with correlationId: {}", correlationId);
        
        return chain.filter(exchange)
            .contextWrite(context -> {
                MDCContextLifter.addCorrelationIdToContext(context, correlationId);
                return context;
            })
            .doFinally(signalType -> {
                log.debug("Completed request with correlationId: {}", correlationId);
                MDCContextLifter.clearCorrelationId();
            });
    }
} 