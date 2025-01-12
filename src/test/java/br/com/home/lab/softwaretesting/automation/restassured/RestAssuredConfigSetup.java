package br.com.home.lab.softwaretesting.automation.restassured;

import br.com.home.lab.softwaretesting.automation.model.converter.jackson.mapper.CustomObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;

import java.lang.reflect.Type;

public class RestAssuredConfigSetup {
    public static void configureRestAssured() {
        ObjectMapper customObjectMapper = CustomObjectMapper.createObjectMapper();

        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig()
                        .jackson2ObjectMapperFactory(new DefaultJackson2ObjectMapperFactory() {
                            @Override
                            public ObjectMapper create(Type cls, String charset) {
                                return customObjectMapper;
                            }
                        })
                );
    }
}
