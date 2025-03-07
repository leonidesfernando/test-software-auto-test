package br.com.home.lab.softwaretesting.automation.model.converter;

import br.com.home.lab.softwaretesting.automation.util.Constants;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateConverter implements Converter<String, Date> {

    @SneakyThrows
    @Override
    public Date convert(String dateString) {
        if(null == dateString || dateString.isBlank())
            return null;
        try {
            return new SimpleDateFormat(Constants.DD_MM_YYYY_SLASH).parse(dateString);
        }catch (ParseException e){
            try {
                return new SimpleDateFormat(Constants.YYYY_MM_DD_SLASH).parse(dateString);
            }catch (ParseException ee){
                return  new SimpleDateFormat(Constants.YYYY_MMM_DD_DASH).parse(dateString);
            }
        }
    }
}

