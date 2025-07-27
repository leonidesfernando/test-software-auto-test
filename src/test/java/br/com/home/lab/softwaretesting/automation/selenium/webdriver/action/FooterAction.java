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
        assertTrue(page.getTitle().getText().contains(Util.getMessageByKey("leonides.fernando.lab")));
    }
}