package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Getter
public class FooterPage extends BasePage{

    private final By footerTitleBy = By.cssSelector("p[data-qa='footer-title']");

    public FooterPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected boolean isReady() {
        return getFooterTitle().isEnabled() && getFooterTitle().isDisplayed();
    }

    public WebElement getFooterTitle() {
        return SeleniumUtil.findElementBy(getWebDriver(), footerTitleBy);
    }
}
