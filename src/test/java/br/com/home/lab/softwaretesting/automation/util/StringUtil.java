package br.com.home.lab.softwaretesting.automation.util;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public final class StringUtil {

    public String removeAccents(String text){
        CharSequence cs = new StringBuilder(text);
        return Normalizer.normalize(cs, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     *
     *
     * @param dateStr
     * @param inputFormat
     * @param outputFormat
     * @return
     */
    public String stringDateAsFormat(String dateStr, String inputFormat, String outputFormat){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);

        LocalDate date = LocalDate.parse(dateStr, inputFormatter);

       return date.format(outputFormatter);
    }
}
