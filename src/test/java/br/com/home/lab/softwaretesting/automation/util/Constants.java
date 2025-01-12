package br.com.home.lab.softwaretesting.automation.util;

import java.util.Locale;

public interface Constants {

    String TOKEN = "sessionId";
    String ENTRIES_DESCRIPTION = "entriesDescription";
    String ENTRIES_IDS = "entriesIds";

    String dd_MM_yyyy_SLASH = "dd/MM/yyyy";
    String yyyy_MM_dd_SLASH = "yyyy/MM/dd";

    String yyyy_MMM_dd_DASH = "yyyy-MM-dd";

    String EN = "EN";
    String BR = "BR";

    Locale BR_Locale = new Locale("pt", "BR");
    Locale EN_Locale = Locale.US;

}
