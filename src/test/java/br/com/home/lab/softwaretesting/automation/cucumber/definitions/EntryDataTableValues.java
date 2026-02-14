package br.com.home.lab.softwaretesting.automation.cucumber.definitions;

import br.com.home.lab.softwaretesting.automation.model.converter.MoneyToStringConverter;
import br.com.home.lab.softwaretesting.automation.util.DataGen;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

@AllArgsConstructor
public enum EntryDataTableValues {

    ANY_DATE("@AnyDate"){
        @Override
        public String value() {
            return DataGen.strDate();
        }
    },
    DATE_CURRENT_MONTH("@DateCurrentMonth"){
        @Override
        public String value() {
            return DataGen.strDateCurrentMonth();
        }
    },
    MONEY_VALUE("@MoneyValue"){
        @Override
        public String value() {
            MoneyToStringConverter converter = new MoneyToStringConverter();
            return converter.convert(DataGen.amount()
                    .setScale(2, RoundingMode.HALF_UP));
        }
    };

    private final String dataType;

    abstract String value();

    public static EntryDataTableValues from(String value) {

        return Stream.of(EntryDataTableValues.values())
                .filter(l -> l.dataType.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Value " + value + " does not exists."));
    }
}
