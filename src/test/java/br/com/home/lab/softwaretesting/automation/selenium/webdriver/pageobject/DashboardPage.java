package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DashboardPage extends BasePage{

    private By tableChartBy = By.id("tableChart");

    @Getter
    private By pieChartBy = By.id("doughnutChart");

    @Getter
    private By btnListBy = By.cssSelector("a[title='Entries']");

    private By initalDateBy = By.cssSelector("input[name=startDate]");
    private By finalDateBy = By.cssSelector("input[name=endDate]");
    private By btnFilterBy = By.id("bth-search");

    @Getter
    private By sectionTitleBy = By.cssSelector("#app div.card-header > h4");


    private By alertBy = By.cssSelector("div.alert");
    @Getter
    private By alertCloseButtonBy = By.cssSelector("div.alert button");

    public DashboardPage(WebDriver webDriver) {
        super(webDriver);
    }

    public WebElement getBtnList() {
        return SeleniumUtil.findElementBy(getWebDriver(), btnListBy);
    }

    public WebElement getInitalDate() {
        return SeleniumUtil.findElementBy(getWebDriver(), initalDateBy);
    }

    public WebElement getFinalDate() {
        return SeleniumUtil.findElementBy(getWebDriver(), finalDateBy);
    }

    public WebElement getBtnFilter() {
        return SeleniumUtil.findElementBy(getWebDriver(), btnFilterBy);
    }

    public WebElement getBtnAlertClose() {
        SeleniumUtil.waitForElementVisible(getWebDriver(), alertCloseButtonBy);
        return SeleniumUtil.findElementBy(getWebDriver(), alertCloseButtonBy);
    }

    protected boolean isReady(){
        return getBtnList().isDisplayed();
    }
}