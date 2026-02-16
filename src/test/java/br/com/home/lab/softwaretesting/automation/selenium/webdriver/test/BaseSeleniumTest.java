package br.com.home.lab.softwaretesting.automation.selenium.webdriver.test;

import br.com.home.lab.softwaretesting.automation.config.Configurations;
import br.com.home.lab.softwaretesting.automation.model.User;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.action.HomeAction;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.action.LoginAction;
import br.com.home.lab.softwaretesting.automation.util.LoadConfigurationUtil;
import br.com.home.lab.softwaretesting.automation.util.ScenarioContextData;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.util.Strings;

import java.util.Objects;
import java.util.concurrent.Semaphore;

@Slf4j
//@Listeners({ScreenshotListener.class})
public abstract class BaseSeleniumTest {

    private final Semaphore semaphore = new Semaphore(1);
    private static final String BROWSER = "browser";
    private static final Configurations config = ConfigFactory.create(Configurations.class);

    private final String language;

    protected final ScenarioContextData context = new ScenarioContextData();

    private User loggedUser;
    protected static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();


    public BaseSeleniumTest() {
        loggedUser = LoadConfigurationUtil.getUser();
        language = LoadConfigurationUtil.getLanguage();
    }

    public synchronized WebDriver getWebDriver() {
        return webDriver.get();
    }

    @BeforeSuite
    protected void beforeSuite() {
    }

    public void login() {
        login(loggedUser);
    }

    @Step("Performing the log in with user {user.username} and password ***")
    public void login(User user) {
        loggedUser = user;
        access();
        doLogin();
    }

    protected void doLogin() {
        LoginAction loginAction = new LoginAction(getWebDriver());
        loginAction.doLogin(loggedUser, language);
    }

    @Step("Performing logout the current user")
    public void doLogout() {
        HomeAction homeAction = new HomeAction(getWebDriver());
        homeAction.doLogout();
    }


    @Step("Asking the browser to access the URL loaded from LoadConfigurationUtil")
    protected void access() {
        try {
            getWebDriver().get(LoadConfigurationUtil.getAppUrl());
        } catch (RuntimeException e) {
            throw new IllegalStateException(String.format("Fail to access %s check if the app is alive and your internet connection", LoadConfigurationUtil.getAppUrl()),e);
        }
    }

    @Step("Closing the browser")
    @AfterClass
    protected void finishDriver() {
        try {
            semaphore.acquire();
            WebDriver driver = getWebDriver();
            if (driver != null) {
                driver.quit();
                //driver.close();
            }
            webDriver.remove();
            semaphore.release();
            log.info("Driver and browser closed.");
        } catch (Exception e) {
            throw new IllegalStateException("Failure to finish webDriver: ",e);
        }
    }

    @Step("Initializing browser and screenshot listener")
    @BeforeClass
    protected void init() {
        try {
            semaphore.acquire();
            WebDriver driver = loadWebDriver();
            Objects.requireNonNull(driver);
            webDriver.set(ThreadGuard.protect(driver));
            semaphore.release();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    protected WebDriver loadWebDriver(){
        WebDriver driver = getBrowser().loadBrowser();
        closeBrowserWhenThreadEnds(driver);
        return driver;
    }

    private void closeBrowserWhenThreadEnds(WebDriver driver){
        Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
    }

    private Browser getBrowser(){
        String browser = System.getProperty(BROWSER);
        if(Strings.isNotNullAndNotEmpty(browser)) {
            return Browser.valueOf(browser.toUpperCase());
        }
        return config.browser();
    }
}