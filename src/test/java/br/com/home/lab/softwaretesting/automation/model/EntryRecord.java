package br.com.home.lab.softwaretesting.automation.model;

import lombok.NonNull;

public record EntryRecord(@NonNull String description, @NonNull String entryDate, @NonNull EntryType type) {
}
