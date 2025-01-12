package br.com.home.lab.softwaretesting.automation.config;


import br.com.home.lab.softwaretesting.automation.selenium.webdriver.test.Browser;
import org.aeonbits.owner.Config;


@Config.Sources({
        "classpath:configuration.properties"
})
public interface Configurations extends Config {

    String url();

    boolean headless();

    Browser browser();

    String username();

    String password();

    String language();
}
