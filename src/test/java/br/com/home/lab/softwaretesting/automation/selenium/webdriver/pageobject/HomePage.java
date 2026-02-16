package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Getter
public class HomePage extends BasePage {


    private By logoutLinkBy = By.id("logout");

    private By brLanguageBy = By.id("BR");

    private By enLanguageBy = By.id("EN");

    private By nameUser = By.cssSelector("span[qadata='nameUser']");

    public HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected boolean isReady() {
        return SeleniumUtil.findElementBy(getWebDriver(), logoutLinkBy).isEnabled();
    }

    public WebElement getLogoutLink() {
        return SeleniumUtil.findElementBy(getWebDriver(), logoutLinkBy);
    }
    public WebElement getBrLanguage() {
        return SeleniumUtil.findElementBy(getWebDriver(), brLanguageBy);
    }
    public WebElement getEnLanguage() {
        return SeleniumUtil.findElementBy(getWebDriver(), enLanguageBy);
    }
    public WebElement getNameUserWebElement() {
        return SeleniumUtil.findElementBy(getWebDriver(), nameUser);
    }
}
