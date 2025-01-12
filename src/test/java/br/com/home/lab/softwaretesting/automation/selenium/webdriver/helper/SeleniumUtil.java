package br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

@Slf4j
public class SeleniumUtil {

    private static final int DEFAULT_TIMEOUT_SECONDS = 60;
    private static final int DEFAULT_POLLING_SECONDS = 2;

    public static WebElement waitForElementVisible(WebDriver driver, WebElement element) {
        return fluentWait(driver)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static Wait<WebDriver> fluentWait(WebDriver driver){
        return fluentWait(driver,
                DEFAULT_TIMEOUT_SECONDS,
                DEFAULT_POLLING_SECONDS);
    }

    public static Boolean waitForElementInvisible(WebDriver driver, WebElement element) {
        return fluentWait(driver)
                .until(ExpectedConditions.invisibilityOf(element));
    }

    public static Wait<WebDriver> fluentWait(WebDriver driver, int timeout, int polling) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(polling))
                .ignoring(StaleElementReferenceException.class);
    }

    public static WebElement waitForPresenceOfXpath(WebDriver driver, String xpath) {
        return waitForPresenceBy(driver, By.xpath(xpath));
    }

    public static WebElement waitForPresenceOfId(WebDriver driver, String id) {
        return waitForPresenceBy(driver, By.id(id));
    }

    public static void waitAjaxCompleted(WebDriver driver){
        fluentWait(driver)
                .until(dr -> String
                        .valueOf(((JavascriptExecutor) dr).executeScript(
                                "return document.readyState")
                        )
                        .equals("complete"));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(200));
    }

    public static void fillOutInputWithJS(WebDriver driver, WebElement input, String value){

        waitForElementVisible(driver, input);
        String script = "let element = arguments[0];"
                + "let value = arguments[1];"
                + "element.value = value;"
                + "element.dispatchEvent(new Event('input'));"
                + "element.dispatchEvent(new Event('change'));";

        ((JavascriptExecutor) driver).executeScript(script, input, value);
    }

    private static WebElement waitForPresenceBy(WebDriver driver, By by) {
        return fluentWait(driver)
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }
}
