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
            return new SimpleDateFormat(Constants.dd_MM_yyyy_SLASH).parse(dateString);
        }catch (ParseException e){
            try {
                return new SimpleDateFormat(Constants.yyyy_MM_dd_SLASH).parse(dateString);
            }catch (ParseException ee){
                return  new SimpleDateFormat(Constants.yyyy_MMM_dd_DASH).parse(dateString);
            }
        }
    }
}

