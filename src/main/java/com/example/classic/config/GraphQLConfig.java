package com.example.classic.config;

import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.classic.scalar.DateTimeScalar;
import com.example.classic.scalar.JsonScalar;

@Configuration
public class GraphQLConfig {
    
    @Bean
    public GraphQLScalarType dateTimeScalar() {
        return DateTimeScalar.DATE_TIME;
    }

    @Bean
    public GraphQLScalarType jsonScalar(JsonScalar jsonScalar) {
        return jsonScalar.json();
    }
} 