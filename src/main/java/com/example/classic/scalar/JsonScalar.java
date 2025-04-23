package com.example.classic.scalar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JsonScalar {
    private final ObjectMapper objectMapper;

    public JsonScalar(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GraphQLScalarType json() {
        return GraphQLScalarType.newScalar()
            .name("JSON")
            .description("A JSON object")
            .coercing(new Coercing<Map<String, Object>, String>() {
                @Override
                public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    try {
                        return objectMapper.writeValueAsString(dataFetcherResult);
                    } catch (JsonProcessingException e) {
                        throw new CoercingSerializeException("Unable to serialize JSON", e);
                    }
                }

                @Override
                public Map<String, Object> parseValue(Object input) throws CoercingParseValueException {
                    try {
                        if (input instanceof String) {
                            return objectMapper.readValue((String) input, new TypeReference<Map<String, Object>>() {});
                        }
                        throw new CoercingParseValueException("Expected String");
                    } catch (Exception e) {
                        throw new CoercingParseValueException("Invalid JSON format", e);
                    }
                }

                @Override
                public Map<String, Object> parseLiteral(Object input) throws CoercingParseLiteralException {
                    if (input instanceof String) {
                        try {
                            return objectMapper.readValue((String) input, new TypeReference<Map<String, Object>>() {});
                        } catch (Exception e) {
                            throw new CoercingParseLiteralException("Invalid JSON format", e);
                        }
                    }
                    throw new CoercingParseLiteralException("Expected String");
                }
            })
            .build();
    }
} 