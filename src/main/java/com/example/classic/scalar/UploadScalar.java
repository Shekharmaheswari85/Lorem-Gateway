package com.example.classic.scalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

@Configuration
public class UploadScalar {
    @Bean
    public GraphQLScalarType upload() {
        return GraphQLScalarType.newScalar()
                .name("Upload")
                .description("Upload custom scalar type")
                .coercing(new Coercing<MultipartFile, MultipartFile>() {
                    @Override
                    public MultipartFile serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof MultipartFile) {
                            return (MultipartFile) dataFetcherResult;
                        }
                        throw new CoercingSerializeException("Expected MultipartFile");
                    }

                    @Override
                    public MultipartFile parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof MultipartFile) {
                            return (MultipartFile) input;
                        }
                        throw new CoercingParseValueException("Expected MultipartFile");
                    }

                    @Override
                    public MultipartFile parseLiteral(Object input) throws CoercingParseLiteralException {
                        throw new CoercingParseLiteralException("Upload scalar cannot be parsed from AST");
                    }
                })
                .build();
    }
} 