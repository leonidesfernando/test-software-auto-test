package br.com.home.lab.softwaretesting.automation.model.record;

import java.util.List;

public record ResultRecord(
        String totalSpend,
        String totalEarnings,
        String grandTotalExpenses,
        String grandTotalWinnings,
        List<EntryRecord> entries,
        int p,
        int pageSize,
        long totalRecords,
        List<Integer> pages,
        String itemSearch
) {
}
