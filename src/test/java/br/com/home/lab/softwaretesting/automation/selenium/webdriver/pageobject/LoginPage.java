package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.model.User;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Getter
public class LoginPage extends BasePage {

    private By usernameInputBy = By.name("username");

    private By passwordInputBy = By.name("password");

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void submitUserCredentials(User user){
        getUsernameInput().clear();
        getUsernameInput().sendKeys(user.username());
        getPasswordInput().clear();
        getPasswordInput().sendKeys(user.password());
        getPasswordInput().sendKeys(Keys.RETURN);
    }

    @Override
    protected boolean isReady() {
        return getUsernameInput().isEnabled() &&
                getPasswordInput().isEnabled();
    }

    public WebElement getUsernameInput() {
        return SeleniumUtil.findElementBy(getWebDriver(), usernameInputBy);
    }
    public WebElement getPasswordInput() {
        return SeleniumUtil.findElementBy(getWebDriver(), passwordInputBy);
    }
}