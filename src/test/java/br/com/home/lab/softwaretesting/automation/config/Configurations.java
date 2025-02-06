package br.com.home.lab.softwaretesting.automation.config;


import br.com.home.lab.softwaretesting.automation.selenium.webdriver.test.Browser;
import org.aeonbits.owner.Config;


@Config.Sources({
        "classpath:configuration.properties"
})
public interface Configurations extends Config {

    @Key("app.url")
    String appUrl();

    @Key("api.url")
    String apiUrl();

    boolean headless();

    Browser browser();

    String username();

    String password();

    String language();
}
