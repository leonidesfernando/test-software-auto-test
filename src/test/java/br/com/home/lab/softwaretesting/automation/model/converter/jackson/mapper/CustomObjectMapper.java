package br.com.home.lab.softwaretesting.automation.model.converter.jackson.mapper;

import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.model.converter.CategoryDeserialize;
import br.com.home.lab.softwaretesting.automation.model.converter.EntryTypeDeserialize;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomObjectMapper {
    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Category.class, new CategoryDeserialize());
        module.addDeserializer(EntryType.class, new EntryTypeDeserialize());
        mapper.registerModule(module);
        return mapper;
    }
}