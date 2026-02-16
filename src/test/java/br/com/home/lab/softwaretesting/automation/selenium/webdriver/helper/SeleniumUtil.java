package br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.Duration;
import java.util.Comparator;
import java.util.stream.Stream;

@Slf4j
public class SeleniumUtil {

    private static final int DEFAULT_TIMEOUT_SECONDS = 60;
    private static final int DEFAULT_POLLING_SECONDS = 1;

    public static void waitTillUrlContains(WebDriver driver, String fractionUrl) {
        fluentWait(driver).until(ExpectedConditions.urlContains(fractionUrl));
    }

    public static void waitToBeClickable(WebDriver driver, By by) {
        fluentWait(driver).until(ExpectedConditions.elementToBeClickable(by));
    }

    public static void scrollToElement(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        fluentWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement findElementBy(WebDriver driver, By by) {
        return driver.findElement(by);
    }

    public static WebElement waitForElementVisible(WebDriver driver, WebElement element) {
        return fluentWait(driver)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForElementVisible(WebDriver driver, By by) {
        return fluentWait(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static Wait<WebDriver> fluentWait(WebDriver driver){
        return fluentWait(driver,
                DEFAULT_TIMEOUT_SECONDS,
                DEFAULT_POLLING_SECONDS);
    }

    public static Boolean waitForElementInvisible(WebDriver driver, By by) {
        return fluentWait(driver)
                .until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public static Boolean waitForElementInvisible(WebDriver driver, WebElement element, int timeoutSeconds) {
        return fluentWait(driver, timeoutSeconds, 200)
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

    public static void jsClick(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public static WebElement waitForPresenceBy(WebDriver driver, By by) {
        return fluentWait(driver)
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static Path waitForExcelFile(Duration timeout) throws IOException, InterruptedException {
        Path dir = Paths.get(SeleniumBootstrap.getDownloadDir());
        Path candidate = null;
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            dir.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);  // Watch both for stability

            long deadline = System.currentTimeMillis() + timeout.toMillis();


            while (System.currentTimeMillis() < deadline) {
                // Try reactive first: take() blocks until event or interrupt
                WatchKey key = watchService.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS);  // Short timeout → non-blocking
                if (key != null) {
                    // Event detected! Process it
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.OVERFLOW) continue;  // Skip buffer overflow

                        @SuppressWarnings("unchecked")
                        Path filename = ((WatchEvent<Path>) event).context();
                        Path fullPath = dir.resolve(filename);

                        if (isExcelFile(fullPath)) {
                            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                                candidate = fullPath;
                            }

                            // If stable (no recent modify), we're done
                            if (candidate != null && fullPath.equals(candidate) &&
                                    System.currentTimeMillis() - fullPath.toFile().lastModified() >= 1500) {
                                key.reset();
                                return candidate;
                            }
                        }
                    }
                    key.reset();  // Critical: Re-arm for next events
                } else {
                    try (Stream<Path> stream = Files.list(dir)) {
                        candidate = stream
                                .filter(p -> isExcelFile(p))
                                .max(Comparator.comparingLong(p -> p.toFile().lastModified()))
                                        .orElse(null);   // or handle empty directory
                    }

                    if (candidate != null) {
                        return candidate;
                    }
                }
            }
        }

        return candidate;
    }

    private static boolean isExcelFile(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".xlsx") || name.endsWith(".xls");
    }
}
