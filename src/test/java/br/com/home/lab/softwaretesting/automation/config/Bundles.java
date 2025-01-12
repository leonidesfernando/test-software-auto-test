package br.com.home.lab.softwaretesting.automation.config;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static br.com.home.lab.softwaretesting.automation.util.Constants.*;

@UtilityClass
public final class Bundles {
    private static final String MESSAGE_BUNDLE_NAME = "messages";


    public String getMessage(String key, String language){
        return getKey(getBundle(language), key);
    }

    private ResourceBundle getBundle(String language){
        return ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, getLocale(language));
    }

    private Locale getLocale(String language){
        switch (language){
            case EN:
                return EN_Locale;
            case BR:
                return BR_Locale;
            default:
                throw new UnsupportedOperationException(
                        String.format("Language %s not supported. Only the langs %s,%s are supported",
                                language, EN, BR));
        }
    }

    private String getKey(ResourceBundle bundle, String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
