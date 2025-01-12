package br.com.home.lab.softwaretesting.automation.model.converter;

import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.util.StringUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CategoryDeserialize extends JsonDeserializer<Category> {
    @Override
    public Category deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = StringUtil.removeAccents(jsonParser.getText()).toUpperCase()
                .replaceAll("\\u00a0", "")
                .replaceAll("\\s*", "")
                .replace(".", "_")
                .replace("&","_");

        return Category.valueOf(text);
    }
}
