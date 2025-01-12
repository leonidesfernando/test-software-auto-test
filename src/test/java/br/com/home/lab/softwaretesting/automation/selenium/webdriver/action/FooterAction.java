package br.com.home.lab.softwaretesting.automation.selenium.webdriver.action;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.FooterPage;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertEquals;

public class FooterAction extends BaseAction<FooterPage> {

    public FooterAction(WebDriver webDriver) {
        super(webDriver, new FooterPage(webDriver));
    }

    @Override
    public FooterPage getPage() {
        return page;
    }

    public void checkTitle(){
        assertEquals(page.getTitle().getText(), "© 2021 Leonides Fernando's Lab");
    }
}