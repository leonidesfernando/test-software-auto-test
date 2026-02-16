package br.com.home.lab.softwaretesting.automation.util;

import br.com.home.lab.softwaretesting.automation.config.Bundles;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;

@Slf4j
public class Util {

    private Util() {}

    public static String getMessageByKey(String key) {
        return Bundles.getMessage(key, LoadConfigurationUtil.getLanguage());
    }

    public static DateTimeFormatter getDateFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }


    public static void sleep(long delayMs) throws InterruptedException {
        log.warn("Returning the execution in {}ms...", delayMs);
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Retry interrupted - thread was interrupted after " + delayMs + "ms delay",
                    ie
            );
        }
    }

}
