package br.com.home.lab.softwaretesting.automation.config;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static br.com.home.lab.softwaretesting.automation.util.Constants.*;


public final class Bundles {
    private static final String MESSAGE_BUNDLE_NAME = "messages";


    private Bundles(){}

    public static String getMessage(String key, String language){
        return getKey(getBundle(language), key);
    }

    private static ResourceBundle getBundle(String language){
        return ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, getLocale(language));
    }

    private static Locale getLocale(String language){
        return switch (language) {
            case EN -> EN_Locale;
            case BR -> BR_Locale;
            default -> throw new UnsupportedOperationException(
                    String.format("Language %s not supported. Only the langs %s,%s are supported",
                            language, EN, BR));
        };
    }

    private static String getKey(ResourceBundle bundle, String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
