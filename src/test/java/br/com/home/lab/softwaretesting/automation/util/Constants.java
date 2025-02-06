package br.com.home.lab.softwaretesting.automation.util;

import java.util.Locale;

public interface Constants {

    String AUTH_TOKEN = "authToken";

    String dd_MM_yyyy_SLASH = "dd/MM/yyyy";
    String yyyy_MM_dd_SLASH = "yyyy/MM/dd";

    String yyyy_MMM_dd_DASH = "yyyy-MM-dd";

    String EN = "EN";
    String BR = "BR";

    Locale BR_Locale = new Locale("pt", "BR");
    Locale EN_Locale = Locale.US;


    String ENTRIES = "lancamentos";
    String DESCRIPTION = "description";
    String AMOUNT = "amount";
    String ENTRY_DATE = "entryDate";
    String ENTRY_TYPE = "entryType";
    String CATEGORY = "category";
}
