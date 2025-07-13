package br.com.home.lab.softwaretesting.automation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Map;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils(){}

    public static Map<String, String> jsonToKeyValue(String secretJson){
        try{
            return Collections.unmodifiableMap(MAPPER.readValue(secretJson, new TypeReference<>() {
            }));
        }catch (JsonProcessingException e){
            throw new IllegalStateException("Incorrect JSON String: ", e);
        }
    }

    public static String convertToString(Object object){
        try {
            return MAPPER.writeValueAsString(object);
        }catch (JsonProcessingException e){
            throw new IllegalStateException("Fail parsing object to JSON", e);
        }
    }
}
