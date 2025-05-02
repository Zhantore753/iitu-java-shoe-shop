package com.example.shoeshop.deserializer;

import com.example.shoeshop.exception.InvalidInputException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String value = node.asText();

        try {
            BigDecimal decimal = new BigDecimal(value);

            // Validate the decimal value
            if (decimal.compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidInputException("Price cannot be negative");
            }

            return decimal;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid price format: " + value);
        }
    }
}
