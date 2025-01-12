package br.com.home.lab.softwaretesting.automation.cucumber.definitions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum LoginDataTableValues {
    VALID_USER("@ValidUser"),
    INVALID_USER("@InvalidUser"),

    VALID_PASSWORD("@ValidPassword"),

    INVALIDP_ASSWORD("@InvalidPassword");

    @Getter
    private final String item;

    public static LoginDataTableValues from(String value) {

        return Stream.of(LoginDataTableValues.values())
                .filter(l -> l.item.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Login item " + value + " does not exists."));
    }
}