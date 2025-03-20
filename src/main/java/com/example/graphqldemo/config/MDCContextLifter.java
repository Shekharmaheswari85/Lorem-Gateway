package com.example.graphqldemo.config;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.HashMap;

public class MDCContextLifter {
    private static final String MDC_CONTEXT_KEY = "MDC_CONTEXT";

    public static Consumer<Signal<?>> liftContext() {
        return signal -> {
            ContextView context = signal.getContextView();
            @SuppressWarnings("unchecked")
            Map<String, String> contextMap = (Map<String, String>) context.getOrEmpty(MDC_CONTEXT_KEY)
                    .map(map -> map instanceof Map<?, ?> ? new HashMap<>((Map<String, String>) map) : Map.of())
                    .orElse(Map.of());
            MDC.setContextMap(contextMap);
        };
    }

    public static Context withContext(Map<String, String> map) {
        return Context.of(MDC_CONTEXT_KEY, map);
    }

    public static Context withContext() {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        if (contextMap == null) {
            return Context.empty();
        }
        return withContext(contextMap);
    }

    public static Map<String, String> getContextMap() {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        if (contextMap == null) {
            return Map.of();
        }
        return contextMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
} 