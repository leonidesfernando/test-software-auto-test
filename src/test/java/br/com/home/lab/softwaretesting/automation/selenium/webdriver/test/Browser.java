package br.com.home.lab.softwaretesting.automation.selenium.webdriver.test;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumBootstrap;
import org.openqa.selenium.WebDriver;

public enum Browser {
    CHROME{
        @Override
        WebDriver loadBrowser() {
            return SeleniumBootstrap.setupChrome();
        }
    },
    EDGE {
        @Override
        WebDriver loadBrowser() {
            return SeleniumBootstrap.setupEdge();
        }
    },
    FIREFOX {
        @Override
        WebDriver loadBrowser() {
            return SeleniumBootstrap.setupFirefox();
        }
    };

    abstract WebDriver loadBrowser();
}
