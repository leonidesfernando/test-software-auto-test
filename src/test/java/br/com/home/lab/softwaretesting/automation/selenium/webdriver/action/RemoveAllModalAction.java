package br.com.home.lab.softwaretesting.automation.selenium.webdriver.action;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.RemoveAllModalPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil.waitForElementInvisible;

public class RemoveAllModalAction extends BaseAction<RemoveAllModalPage> {

    public RemoveAllModalAction(WebDriver webDriver) {
        super(webDriver, new RemoveAllModalPage(webDriver));
    }

    @Step("Modal -  Removing all entries, confirming and managing AJAX")
    public void removeAllEntries() {
        page.get();

        final String text = page.getTextRemoveAllSystem()
                .getAttribute("value");
        page.getTextDeleteAll().sendKeys(text);
        page.getBtnYesRemoveAll().click();
        waitForElementInvisible(getWebDriver(), page.getBtnYesRemoveAllBy());
    }

    @Step("Modal - Do not remove all entries, clicking cancel and managing AJAX")
    public void doNotRemoveAllEntries() {
        page.get();
        SeleniumUtil.waitForPresenceBy(getWebDriver(), page.getBtnCancelRemoveAllBy());
        page.getBtnCancelRemoveAll().click();
        waitForElementInvisible(getWebDriver(), page.getBtnCancelRemoveAllBy());
    }

    @Override
    public RemoveAllModalPage getPage() {
        return page;
    }
}
