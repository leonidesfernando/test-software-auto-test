package br.com.home.lab.softwaretesting.automation.selenium.webdriver.config;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.test.BaseSeleniumTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ScreenshotListener extends TestListenerAdapter {


    @Override
    public void onTestFailure(ITestResult result) {
        super.onTestFailure(result);
        if (!result.isSuccess() && result.getInstance() instanceof BaseSeleniumTest) {
            final var methodName = result.getName();
            log.info("Saving screenshot for test case: {}", methodName);
            WebDriver driver = getWebDriver((BaseSeleniumTest)result.getInstance());
            final String path = "/target/surefire-reports/";

            try {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + path;
                log.info("Saving screenshot for test case: {} in {}", methodName, reportDirectory);
                String className = result.getTestClass().getName();
                className = className.substring(1 + className.lastIndexOf("."));
                final String fileName = className + "_" + methodName + "_"
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm_ss")) + ".png";

                File destFile = new File(reportDirectory + "/failure_screenshots/"
                        + fileName
                );
                FileUtils.copyFile(srcFile, destFile);

                Reporter.log("<a href='" +
                        destFile.getAbsolutePath() + "'> <img src='"
                        + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>"
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private WebDriver getWebDriver(BaseSeleniumTest baseSeleniumTest) {
        return baseSeleniumTest.getWebDriver();
    }
}
