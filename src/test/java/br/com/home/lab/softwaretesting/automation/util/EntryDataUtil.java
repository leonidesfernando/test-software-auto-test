package br.com.home.lab.softwaretesting.automation.util;

import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.Entry;
import br.com.home.lab.softwaretesting.automation.model.EntryType;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public final class EntryDataUtil {

    private static final EntryType[] entryTypes = EntryType.values();

    private static final Category[] categories = Category.values();

    private EntryDataUtil(){}

    public static Entry newValidEntry(){
        String description = new StringJoiner(" ")
                .add("Assured Rest")
                .add(DataGen.productName())
                .add(LocalDateTime.now().toString()).toString();

        return new Entry.EntryBuilder()
                .description(description)
                .amount(DataGen.amount())
                .entryType(entryTypes[DataGen.number(0, entryTypes.length-1)])
                .entryDate(DataGen.dateCurrentMonth())
                .category(categories[DataGen.number(0, categories.length-1)])
                .build();
    }

    public static Entry getUpdateEntryData(long id, Entry entry){
        return new Entry.EntryBuilder()
                .id(id)
                .description(entry.getDescription())
                .amount(entry.getAmount())
                .entryDate(entry.getEntryDate())
                .entryType(entry.getEntryType())
                .category(entry.getCategory())
                .userId(entry.getUserId())
                .build();
    }
}
