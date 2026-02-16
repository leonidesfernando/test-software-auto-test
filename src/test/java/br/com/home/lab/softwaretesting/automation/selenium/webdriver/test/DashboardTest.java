package br.com.home.lab.softwaretesting.automation.selenium.webdriver.test;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.action.DashboardAction;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.action.EntriesListAction;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import br.com.home.lab.softwaretesting.automation.util.Constants;
import br.com.home.lab.softwaretesting.automation.util.DataGen;
import br.com.home.lab.softwaretesting.automation.util.Util;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.testng.Tag;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static br.com.home.lab.softwaretesting.automation.util.Constants.REGRESSION_TEST;

@Epic("Regression Tests Epic")
@Feature("Navigating through the Dashboard")
public class DashboardTest extends BaseSeleniumTest {

    public DashboardTest() {
        super();
    }

    @Description("With a valid credentials user must be able to log into the system")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void loginLancamentos() {
        super.login();
    }

    @Description("After performing the login the user goes to the expense and income entries list, so he can access the button to go to the Dashboard")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.CRITICAL)
    @Test(dependsOnMethods = "loginLancamentos")
    public void accessDashboard() {

        EntriesListAction entriesListAction = new EntriesListAction(getWebDriver());
        entriesListAction.gotToDashboard();
    }

    @Test(dependsOnMethods = "accessDashboard")
    public void filterDashboardByDate() {
        SeleniumUtil.waitTillUrlContains(getWebDriver(), "dashboard");
        DashboardAction dashboardAction = new DashboardAction(getWebDriver());

        Pair<String, String> dates = getInitialAndFinalDate();
        dashboardAction.filterByDate(dates.getLeft(), dates.getRight());
    }

    @Test(dependsOnMethods = "accessDashboard")
    public void filterDashboardByWrongDate() {
        SeleniumUtil.waitTillUrlContains(getWebDriver(), "dashboard");
        DashboardAction dashboardAction = new DashboardAction(getWebDriver());

        Pair<String, String> dates = getInitialAndFinalDate();
        dashboardAction.filterByDate(dates.getRight(), dates.getLeft());
        dashboardAction.closeAlertMessage();
    }

    @Description("A logged user after access the Dashboard page by the button, must be able to return to the expense and income entries list page.")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.CRITICAL)
    @Test(dependsOnMethods = "filterDashboardByWrongDate")
    public void backToList() {
        DashboardAction dashboardAction = new DashboardAction(getWebDriver());
        dashboardAction.goToList();
    }

    @Description("The authenticated user must be able to log out the system")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.CRITICAL)
    @Test(dependsOnMethods = "backToList")
    public void logout() {
        super.doLogout();
    }

    protected Pair<String, String> getInitialAndFinalDate() {
        var initialDateStr = DataGen.strDateCurrentMonthEnglisFormat();
        var finalDateStr = DataGen.strDateCurrentMonthEnglisFormat();

        LocalDate initialDate = LocalDate.parse(initialDateStr, Util.getDateFormatter(Constants.YYYY_MMM_DD_DASH));
        LocalDate finalDate = LocalDate.parse(finalDateStr, Util.getDateFormatter(Constants.YYYY_MMM_DD_DASH));
        if(initialDate.isAfter(finalDate)) {
            var temp = initialDateStr;
            initialDateStr = finalDateStr;
            finalDateStr = temp;
        }
        return Pair.of(initialDateStr, finalDateStr);
    }
}
