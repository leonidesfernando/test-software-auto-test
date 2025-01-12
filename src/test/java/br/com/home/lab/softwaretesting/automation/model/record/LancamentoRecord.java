package br.com.home.lab.softwaretesting.automation.model.record;


import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.EntryType;

public record LancamentoRecord(
        long id,
        String descricao,
        String valor,
        String dataLancamento,
        EntryType entryType,
        Category category) {
}
