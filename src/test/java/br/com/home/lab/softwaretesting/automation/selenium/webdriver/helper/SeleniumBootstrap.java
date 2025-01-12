package br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper;

import br.com.home.lab.softwaretesting.automation.config.Configurations;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;

@UtilityClass
public class SeleniumBootstrap {
    private static final String HEADLESS = "headless";
    private static final Configurations config = ConfigFactory.create(Configurations.class);

    private boolean isHeadlessMode() {
        if (System.getProperty(HEADLESS) == null) {
            return config.headless();
        }
        return Boolean.parseBoolean(System.getProperty(HEADLESS));
    }

    public WebDriver setupExistingBrowser(){
        try {
            return setupFirefox();
        }catch (Exception e){
            return setupChrome();
        }
    }

    public WebDriver setupChrome(){
        WebDriverManager driverManager = WebDriverManager.chromedriver();
        driverManager.setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("disable-extensions");
        options.addArguments("--no-sandbox");

        if (isHeadlessMode()) {
            options.addArguments("--headless");
        }
        acceptSslAndInsecureCerts(options);
        return maximize(new ChromeDriver(options));
    }

    private void acceptSslAndInsecureCerts(MutableCapabilities options){
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
    }

    public WebDriver setupEdge() {
        WebDriverManager driverManager = WebDriverManager.edgedriver();
        driverManager.setup();

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-in-process-stack-traces");
        if (isHeadlessMode()) {
            options.addArguments("--headless");
        }

        acceptSslAndInsecureCerts(options);
        return maximize(new EdgeDriver(options));
    }

    private WebDriver maximize(WebDriver driver){
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver setupFirefox(){
        WebDriverManager driverManager = WebDriverManager.firefoxdriver();
        driverManager.setup();

        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("browser.download.folderList", 2);
        firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
        firefoxProfile.setPreference("browser.download.manager.showAlertOnComplete", false);
        firefoxProfile.setPreference("browser.download.manager.scanWhenDone", false);
        firefoxProfile.setPreference("browser.download.importedFromSqlite", false);
        firefoxProfile.setPreference("browser.download.panel.shown", false);
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.ms-excel");
        firefoxProfile.setPreference("browser.download.manager.focusWhenStarting", false);
        firefoxProfile.setPreference("browser.download.manager.useWindow", false);
        firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);

        FirefoxOptions options = new FirefoxOptions();
        if(isHeadlessMode()){
            options.addArguments("--headless");
        }
        acceptSslAndInsecureCerts(options);
        return maximize(new FirefoxDriver(options));
    }
}
