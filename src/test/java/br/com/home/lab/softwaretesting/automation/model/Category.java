package br.com.home.lab.softwaretesting.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Category {

    @JsonProperty("FOOD")
    FOOD("food"),
    @JsonProperty("WAGE")
    WAGE("wage"),
    @JsonProperty("LEISURE")
    LEISURE("leisure"),
    @JsonProperty("PHONE_INTERNET")
    PHONE_INTERNET ("phone.internet"),
    @JsonProperty("CAR")
    CAR ("car"),
    @JsonProperty("LOAN")
    LOAN("loan"),
    @JsonProperty("INVESTIMENTS")
    INVESTIMENTS("investiments"),
    @JsonProperty("CLOTHING")
    CLOTHING("clothing"),
    @JsonProperty("OTHER")
    OTHER("other");


    @Getter
    private String nome;
}
