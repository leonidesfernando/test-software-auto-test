package br.com.home.lab.softwaretesting.automation.selenium.webdriver.action;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.DashboardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

public class DashboardAction extends BaseAction<DashboardPage> {

    public DashboardAction(WebDriver webDriver) {
        super(webDriver, new DashboardPage(webDriver));
    }

    @Override
    public DashboardPage getPage() {
        return page;
    }

    @Step("On the dashboard page backing to the listing entries page")
    public EntriesListAction goToList(){
        SeleniumUtil.waitForElementVisible(getWebDriver(), page.getBtnListBy());
        page.getBtnList().click();
        return new EntriesListAction(getWebDriver());
    }

    @Step("Filtering the dashboard by date from {initialDate} to {finalDate}")
    public void filterByDate(String initialDate, String finalDate){
        SeleniumUtil.waitForElementVisible(getWebDriver(), page.getBtnListBy());
        SeleniumUtil.fillOutInputWithJS(getWebDriver(), page.getInitalDate(), initialDate);
        SeleniumUtil.fillOutInputWithJS(getWebDriver(), page.getFinalDate(), finalDate);
        page.getBtnFilter().click();
        SeleniumUtil.waitForElementVisible(getWebDriver(), page.getPieChartBy());
    }

    public void closeAlertMessage(){
        SeleniumUtil.waitForElementVisible(getWebDriver(), page.getBtnAlertClose());
        page.getBtnAlertClose().click();
        SeleniumUtil.waitForElementInvisible(getWebDriver(), page.getAlertCloseButtonBy());
    }

    public void validateDashboardPageIsLoaded(){
        WebElement section = SeleniumUtil.waitForElementVisible(getWebDriver(), page.getSectionTitleBy());
        assertThat(section)
                .isNotNull();
        assertThat(section.getText())
                .isEqualTo("Dashboard");
    }
}
