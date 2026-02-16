package br.com.home.lab.softwaretesting.automation.selenium.webdriver.action;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.FooterPage;
import br.com.home.lab.softwaretesting.automation.util.Util;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertTrue;

public class FooterAction extends BaseAction<FooterPage> {

    public FooterAction(WebDriver webDriver) {
        super(webDriver, new FooterPage(webDriver));
    }

    @Override
    public FooterPage getPage() {
        return page;
    }

    public void checkTitle(){
        final String footerText = page.getFooterTitle().getText();
        final String expectedText = Util.getMessageByKey("leonides.fernando.lab");
        assertTrue(footerText.contains(expectedText),
                String.format("Footer title is incorrect. The title is: [%s] and the i18n key is: [%s]", footerText, expectedText));
    }
}