package br.com.home.lab.softwaretesting.automation.jdbi.model;

import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import lombok.NonNull;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Entry(long id,
                    @NonNull String description,
                    @NonNull LocalDate entryDate,
                    @NonNull @ColumnName("entry_type") EntryType type,
                    @NonNull Category category,
                    @NonNull BigDecimal amount) { }
