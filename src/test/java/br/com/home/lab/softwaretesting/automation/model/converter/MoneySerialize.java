package br.com.home.lab.softwaretesting.automation.model.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneySerialize extends JsonSerializer<BigDecimal> {
    private static final int DEFAULT_SCALE = 2;

    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value == null) {
            jsonGenerator.writeNull();
            return;
        }
        BigDecimal normalized = value.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);

        MoneyToStringConverter converter = new MoneyToStringConverter();
        jsonGenerator.writeString(converter.convert(normalized));
    }

    @Override
    public Class<BigDecimal> handledType() {
        return BigDecimal.class;
    }
}
