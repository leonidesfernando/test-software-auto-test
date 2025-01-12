package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Objects;

public abstract class BasePage extends LoadableComponent<BasePage> {

    protected ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    public BasePage(WebDriver webDriver) {
        Objects.requireNonNull(webDriver);
        this.webDriver.set(webDriver);
        PageFactory.initElements(
                new AjaxElementLocatorFactory(getWebDriver(), 20),
                this);
    }

    public WebDriver getWebDriver() {
        WebDriver driver = webDriver.get();
        Objects.requireNonNull(driver);
        return driver;
    }

    /**
     * <p>The default behavior the page do nothing to load, its will loaded by the <i>BaseSeleniumTest</i>,
     * on <code>BaseSeleniumTest::access</code> method. <b>But if will be necessary you can implement this method(load),
     * but be aware what you are doing</b></p>
     * <p>Each BasePage instance must implement the
     * <code>LoadableComponemt::isLoaded</code> method</p>
     */
    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        boolean loaded = false;
        try{
            loaded = isReady();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!loaded){
            throw new Error(String.format("%s is not loaded yet :/", this.getClass().getSimpleName()));
        }
    }

    protected abstract boolean isReady();
}
