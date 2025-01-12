package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class HomePage extends BasePage {


    @FindBy(id = "logout")
    private WebElement logoutLink;

    @FindBy(id = "BR")
    private WebElement brLanguage;

    @FindBy(id = "EN")
    private WebElement enLanguage;

    public HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected boolean isReady() {
        return logoutLink.isEnabled();
    }
}
