package br.com.home.lab.softwaretesting.automation.model;

import br.com.home.lab.softwaretesting.automation.model.converter.CategoryDeserialize;
import br.com.home.lab.softwaretesting.automation.model.converter.EntryTypeDeserialize;
import br.com.home.lab.softwaretesting.automation.model.converter.MoneyDeserialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

import static br.com.home.lab.softwaretesting.automation.util.Constants.dd_MM_yyyy_SLASH;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
@Getter
public class Entry {

    private long id;

    private String description;

    @JsonDeserialize(using = MoneyDeserialize.class)
    private BigDecimal amount;

    @JsonFormat(pattern = dd_MM_yyyy_SLASH)
    private Date entryDate;

    @JsonDeserialize(using = EntryTypeDeserialize.class)
    private EntryType entryType = EntryType.EXPENSE;

    @JsonDeserialize(using = CategoryDeserialize.class)
    private Category category;

    public static class EntryBuilder{
        private long id;
        private String description;
        private BigDecimal amount;
        private Date entryDate;
        private EntryType entryType;
        private Category category;


        public EntryBuilder description(String description){
            this.description = description;
            return this;
        }
        public EntryBuilder amount(BigDecimal amount){
            this.amount = amount;
            return this;
        }
        public EntryBuilder entryType(EntryType entryType){
            this.entryType = entryType;
            return this;
        }
        public EntryBuilder entryDate(Date entryDate){
            this.entryDate = entryDate;
            return this;
        }
        public EntryBuilder category(Category category){
            this.category = category;
            return this;
        }
        public EntryBuilder id(long id){
            this.id = id;
            return this;
        }
        public Entry build(){
            return new Entry(id,description,amount,entryDate,entryType,category);
        }
    }
}