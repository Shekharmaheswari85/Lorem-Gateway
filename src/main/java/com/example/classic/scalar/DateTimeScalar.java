package com.example.classic.scalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeScalar {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static final GraphQLScalarType DATE_TIME = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("A date-time with an offset from UTC/Greenwich in the ISO-8601 calendar system")
            .coercing(new Coercing<OffsetDateTime, String>() {
                @Override
                public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    if (dataFetcherResult instanceof OffsetDateTime) {
                        return ((OffsetDateTime) dataFetcherResult).format(FORMATTER);
                    }
                    throw new CoercingSerializeException("Expected OffsetDateTime");
                }

                @Override
                public OffsetDateTime parseValue(Object input) throws CoercingParseValueException {
                    try {
                        if (input instanceof String) {
                            return OffsetDateTime.parse((String) input, FORMATTER);
                        }
                        throw new CoercingParseValueException("Expected String");
                    } catch (Exception e) {
                        throw new CoercingParseValueException("Invalid DateTime format", e);
                    }
                }

                @Override
                public OffsetDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
                    if (input instanceof String) {
                        try {
                            return OffsetDateTime.parse((String) input, FORMATTER);
                        } catch (Exception e) {
                            throw new CoercingParseLiteralException("Invalid DateTime format", e);
                        }
                    }
                    throw new CoercingParseLiteralException("Expected String");
                }
            })
            .build();
} 