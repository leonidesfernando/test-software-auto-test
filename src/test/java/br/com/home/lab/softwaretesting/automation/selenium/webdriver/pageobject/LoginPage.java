package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.model.User;
import lombok.Getter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class LoginPage extends BasePage {

    @FindBy(name = "username")
    private WebElement usernameInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void submitUserCredentials(User user){
        usernameInput.clear();
        usernameInput.sendKeys(user.username());
        passwordInput.clear();
        passwordInput.sendKeys(user.password());
        passwordInput.sendKeys(Keys.RETURN);
    }

    @Override
    protected boolean isReady() {
        return usernameInput.isEnabled() &&
                passwordInput.isEnabled();
    }
}