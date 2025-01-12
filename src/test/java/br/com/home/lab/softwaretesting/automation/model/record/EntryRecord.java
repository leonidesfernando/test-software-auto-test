package br.com.home.lab.softwaretesting.automation.model.record;

public record EntryRecord(
        long id,
        String description,
        String amount,
        String entryDate,
        String entryType,
        String category
) {
}
