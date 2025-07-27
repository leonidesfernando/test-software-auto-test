package br.com.home.lab.softwaretesting.automation.util;

import br.com.home.lab.softwaretesting.automation.config.Bundles;

public class Util {

    private Util() {}

    public static String getMessageByKey(String key) {
        return Bundles.getMessage(key, LoadConfigurationUtil.getLanguage());
    }
}
