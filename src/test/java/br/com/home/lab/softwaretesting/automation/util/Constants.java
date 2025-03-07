package br.com.home.lab.softwaretesting.automation.util;

import java.util.Locale;

public interface Constants {

    String AUTH_TOKEN = "authToken";

    String DD_MM_YYYY_SLASH = "dd/MM/yyyy";
    String YYYY_MM_DD_SLASH = "yyyy/MM/dd";

    String YYYY_MMM_DD_DASH = "yyyy-MM-dd";

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

    String LOGIN_ENDPOINT = "api/auth/signin";
    String ADD_ENDPOPINT = "/api/entries/add";
    String SEARCH_ENDPOINT = "/api/entries/search";
    String GET_ENDPOINT = "/api/entries/get/%d";
    String UPDATE_ENDPOINT = "/api/entries/update";
    String REMOVE_ENDPOINT = "/api/entries/remove/%d";
    String CHECK_PROFILE_ENDPOINT = "/api/check/profile";


    String SMOKE_TEST = "Smoke";
    String REGRESSION_TEST = "Regression";
}
