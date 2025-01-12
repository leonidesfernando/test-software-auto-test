package br.com.home.lab.softwaretesting.automation.model.converter;

import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.util.StringUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class EntryTypeDeserialize extends JsonDeserializer<EntryType> {
    @Override
    public EntryType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        return EntryType.valueOf(
                StringUtil.removeAccents(jsonParser.getText()).toUpperCase());
    }
}
