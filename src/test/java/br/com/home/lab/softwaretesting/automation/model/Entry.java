package br.com.home.lab.softwaretesting.automation.model;

import br.com.home.lab.softwaretesting.automation.model.converter.CategoryDeserialize;
import br.com.home.lab.softwaretesting.automation.model.converter.MoneyDeserialize;
import br.com.home.lab.softwaretesting.automation.model.converter.EntryTypeDeserialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

import static br.com.home.lab.softwaretesting.automation.util.Constants.dd_MM_yyyy_SLASH;

@EqualsAndHashCode(of = {"id"})
public class Entry {

    @Getter
    private long id;

    @Getter @Setter
    private String description;

    @JsonDeserialize(using = MoneyDeserialize.class)
    @Getter @Setter
    private BigDecimal amount;

    @JsonFormat(pattern = dd_MM_yyyy_SLASH)
    @Getter @Setter
    private Date entryDate;

    @JsonDeserialize(using = EntryTypeDeserialize.class)
    @Getter @Setter
    private EntryType entryType = EntryType.EXPENSE;

    @JsonDeserialize(using = CategoryDeserialize.class)
    @Getter @Setter
    private Category category;
}