package br.com.home.lab.softwaretesting.automation.model.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static br.com.home.lab.softwaretesting.automation.util.Constants.DD_MM_YYYY_SLASH;

public class DateToStringConverter implements Converter<Date, String> {

    @Override
    public String convert(Date date) {
        return Objects.nonNull(date)
                ? new SimpleDateFormat(DD_MM_YYYY_SLASH).format(date)
                : null;
    }
}
