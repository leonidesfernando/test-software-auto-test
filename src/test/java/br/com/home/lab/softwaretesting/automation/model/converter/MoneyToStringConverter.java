package br.com.home.lab.softwaretesting.automation.model.converter;

import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyToStringConverter implements Converter<BigDecimal, String> {
    private static final Locale BR = new Locale("pt", "BR");
    @Override
    public String convert(BigDecimal bigDecimal) {
        if(bigDecimal == null) return "";
        return NumberFormat.getCurrencyInstance(BR).format(bigDecimal);
    }
}

