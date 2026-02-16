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

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class SeleniumBootstrap {
    private static final String HEADLESS = "headless";
    private static final Configurations config = ConfigFactory.create(Configurations.class);
    private static final String donwloadDir;

    static {
        try {
            donwloadDir = Files.createTempDirectory("selenium-downloads").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isHeadlessMode() {
        if (System.getProperty(HEADLESS) == null) {
            return config.headless();
        }
        return Boolean.parseBoolean(System.getProperty(HEADLESS));
    }

    public static String getDownloadDir() {
        return donwloadDir;
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
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--safebrowsing-disable-download-protection");
        options.addArguments("--disable-popup-blocking");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", getDownloadDir());
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.default_content_setting_values.automatic_downloads", 1);

        options.setExperimentalOption("prefs", prefs);

        if (isHeadlessMode()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
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
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-in-process-stack-traces");
        options.addArguments("--window-size=1920,1080");
        if (isHeadlessMode()) {
            options.addArguments("--headless");
            options.addArguments("--headless=new");
        }

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", getDownloadDir());
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);

        options.setExperimentalOption("prefs", prefs);
        acceptSslAndInsecureCerts(options);
        return maximize(new EdgeDriver(options));
    }

    private WebDriver maximize(WebDriver driver){
        driver.manage().window().maximize();
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
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
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.dir", getDownloadDir());
        options.addPreference("browser.download.useDownloadDir", true);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet," +
                        "application/octet-stream");
        options.addPreference("browser.download.manager.showWhenStarting", false);
        options.addPreference("browser.download.manager.focusWhenStarting", false);
        options.addPreference("browser.download.manager.closeWhenDone", true);
        options.addPreference("pdfjs.disabled", true);

        if(isHeadlessMode()){
            options.addArguments("--headless");
        }
        acceptSslAndInsecureCerts(options);
        return maximize(new FirefoxDriver(options));
    }
}
