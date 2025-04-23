package com.example.classic.config;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

import java.util.Map;
import java.util.function.Consumer;

public class MDCContextLifter {
    public static final String CORRELATION_ID = "correlationId";
    private static final String MDC_CONTEXT_KEY = "MDC_CONTEXT";

    public static Consumer<Signal<?>> liftContext() {
        return signal -> {
            if (signal.getContextView().hasKey(MDC_CONTEXT_KEY)) {
                Map<String, String> mdcContext = signal.getContextView().get(MDC_CONTEXT_KEY);
                mdcContext.forEach(MDC::put);
            }
        };
    }

    public static Context putContext(Context context) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        if (mdcContext != null) {
            context = context.put(MDC_CONTEXT_KEY, mdcContext);
        }
        return context;
    }

    public static <T> reactor.util.function.Tuple2<Context, T> addCorrelationIdToContext(Context context, T t) {
        String correlationId = MDC.get(CORRELATION_ID);
        if (correlationId != null) {
            context = context.put(CORRELATION_ID, correlationId);
        }
        return reactor.util.function.Tuples.of(context, t);
    }

    public static <T> T restoreCorrelationIdFromContext(Signal<T> signal) {
        if (signal.getContextView().hasKey(CORRELATION_ID)) {
            MDC.put(CORRELATION_ID, signal.getContextView().get(CORRELATION_ID));
        }
        return signal.get();
    }

    public static void clearCorrelationId() {
        MDC.remove(CORRELATION_ID);
    }
} 