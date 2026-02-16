package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Getter
public class RemoveAllModalPage extends BasePage{

    private By modalRemoveAll = By.id("modalRemoveAll");

    private By btnYesRemoveAllBy = By.id("btnYesRemoveAll");

    private By btnCancelRemoveAllBy = By.id("btnCancelRemoveAll");

    private By waitRemoveAll = By.id("overlayRemoveAll");

    private By textDeleteAllBy = By.id("textDeleteAll");

    private By textRemoveAllSystemBy = By.id("textRemoveAllSystem");

    public RemoveAllModalPage(final WebDriver driver) {
        super(driver);
    }

    public WebElement getTextRemoveAllSystem() {
        return SeleniumUtil.findElementBy(getWebDriver(), textRemoveAllSystemBy);
    }

    public WebElement getTextDeleteAll() {
        return SeleniumUtil.findElementBy(getWebDriver(), textDeleteAllBy);
    }

    public WebElement getBtnYesRemoveAll() {
        return SeleniumUtil.findElementBy(getWebDriver(), btnYesRemoveAllBy);
    }

    public WebElement getBtnCancelRemoveAll() {
        return SeleniumUtil.findElementBy(getWebDriver(), btnCancelRemoveAllBy);
    }

    @Override
    protected boolean isReady() {
        return SeleniumUtil.waitForElementVisible(getWebDriver(), btnYesRemoveAllBy).isDisplayed()
                && SeleniumUtil.waitForElementVisible(getWebDriver(), modalRemoveAll).isEnabled();
    }
}
