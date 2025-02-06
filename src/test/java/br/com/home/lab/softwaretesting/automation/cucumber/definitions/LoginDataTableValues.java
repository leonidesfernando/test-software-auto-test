package br.com.home.lab.softwaretesting.automation.cucumber.definitions;

import br.com.home.lab.softwaretesting.automation.util.DataGen;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum LoginDataTableValues {
    VALID_USER("@ValidUser"){
        @Override
        public String get(String value) {
            return value;
        }
    },
    INVALID_USER("@InvalidUser"){
        @Override
        public String get(String value) {
            return value + DataGen.number(5);
        }
    },
    VALID_PASSWORD("@ValidPassword"){
        @Override
        public String get(String value) {
            return value;
        }
    },
    INVALID_PASSWORD("@InvalidPassword"){
        @Override
        public String get(String value) {
            return DataGen.productName();
        }
    };

    @Getter
    private final String item;

    public abstract String get(String value);

    public static LoginDataTableValues from(String value) {

        return Stream.of(LoginDataTableValues.values())
                .filter(l -> l.item.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Login item " + value + " does not exists."));
    }
}