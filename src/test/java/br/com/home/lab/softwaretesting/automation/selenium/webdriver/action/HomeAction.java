package br.com.home.lab.softwaretesting.automation.selenium.webdriver.action;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.HomePage;
import org.openqa.selenium.WebDriver;

import static br.com.home.lab.softwaretesting.automation.util.Constants.BR;
import static br.com.home.lab.softwaretesting.automation.util.Constants.EN;
import static org.testng.Assert.assertTrue;

public class HomeAction extends BaseAction<HomePage> {

    public HomeAction(WebDriver webDriver) {
        super(webDriver, new HomePage(webDriver));
    }

    @Override
    public HomePage getPage() {
        return page;
    }

    public void doLogout() {
        page.getLogoutLink().click();
        assertTrue(getWebDriver().getCurrentUrl().contains("login"));
    }

    public void selectLanguage(String language){
        switch (language){
            case EN:
                selectEnLanguage();
                break;
            case BR:
                selectBrLanguage();
                break;
            default:
                throw new UnsupportedOperationException(
                        String.format("Language %s not supported. Only the langs %s,%s are supported",
                                language, EN, BR));
        }
    }

    private void selectBrLanguage(){
        page.getBrLanguage().click();
    }

    private void selectEnLanguage(){
        page.getEnLanguage().click();
    }
}