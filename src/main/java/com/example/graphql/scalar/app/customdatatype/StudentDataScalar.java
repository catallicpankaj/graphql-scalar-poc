package com.example.graphql.scalar.app.customdatatype;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import graphql.language.*;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.GraphQLScalarType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class StudentDataScalar {

    @Autowired
    ObjectMapper mapper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Bean
    public GraphQLScalarType jsonObject() {
        return GraphQLScalarType.newScalar()
                .name("JsonObject")
                .description("JSON object")
                .coercing(new Coercing<Object, Object>() {
                    @Override
                    public Object serialize(Object dataFetcherResult) {
                        return dataFetcherResult;
                    }

                    @Override
                    public Object parseValue(Object input) {
                        return input;
                    }

                    @Override
                    public Object parseLiteral(Object input) throws CoercingParseLiteralException {
                        return parseJsonValue(literalOrException(input, ObjectValue.class), Collections.EMPTY_MAP);
                    }

                }).build();
    }

    private static JsonNode parseJsonValue(Value value, Map<String, Object> variables) {
        if (value instanceof BooleanValue) {
            return JsonNodeFactory.instance.booleanNode(((BooleanValue) value).isValue());
        }
        if (value instanceof EnumValue) {
            return JsonNodeFactory.instance.textNode(((EnumValue) value).getName());
        }
        if (value instanceof FloatValue) {
            return JsonNodeFactory.instance.numberNode(((FloatValue) value).getValue());
        }
        if (value instanceof IntValue) {
            return JsonNodeFactory.instance.numberNode(((IntValue) value).getValue());
        }
        if (value instanceof NullValue) {
            return JsonNodeFactory.instance.nullNode();
        }
        if (value instanceof StringValue) {
            return JsonNodeFactory.instance.textNode(((StringValue) value).getValue());
        }
        if (value instanceof ArrayValue) {
            List<Value> values = ((ArrayValue) value).getValues();
            ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode(values.size());
            values.forEach(v -> jsonArray.add(parseJsonValue(v, variables)));
            return jsonArray;
        }
        if (value instanceof VariableReference) {
            return OBJECT_MAPPER.convertValue(variables.get(((VariableReference) value).getName()), JsonNode.class);
        }
        if (value instanceof ObjectValue) {
            final ObjectNode result = JsonNodeFactory.instance.objectNode();
            ((ObjectValue) value).getObjectFields().forEach(objectField ->
                    result.set(objectField.getName(), parseJsonValue(objectField.getValue(), variables)));
            return result;
        }
        //Any other instance throw exception... 
        throw new CoercingParseLiteralException("Unknown scalar AST type: " + value.getClass().getName());
    }

    public static <T extends Value> T literalOrException(Object input, Class<T> valueType) {
        if (valueType.isInstance(input)) {
            return valueType.cast(input);
        }
        throw new CoercingParseLiteralException(errorMessage(input, valueType));
    }

    public static String errorMessage(Object input, Class... allowedTypes) {
        String types = Arrays.stream(allowedTypes)
                .map(type -> "'" + type.getSimpleName() + "'")
                .collect(Collectors.joining(" or "));
        return String.format("Expected %stype %s but was '%s'", input instanceof Value ? "AST " : "",
                types, input == null ? "null" : input.getClass().getSimpleName());
    }


}
