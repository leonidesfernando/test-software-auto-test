package br.com.home.lab.softwaretesting.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EntryType implements DescritivelPorBundle {

/*
    //TODO: fazer o esquema da enum traduzida no projeto gestor - perseu store
            e fazer na categoria tambem*/
    @JsonProperty("INCOME")
    INCOME("income"),
    @JsonProperty("EXPENSE")
    EXPENSE("expense"),
    @JsonProperty("TRANSF")
    TRANSF("transf");

    private String key;

    @Override
    public String getKey() {
        return key;
    }
}
