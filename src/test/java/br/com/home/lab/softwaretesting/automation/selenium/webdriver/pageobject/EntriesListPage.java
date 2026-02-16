package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.components.GridUI;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


@Getter
public class EntriesListPage extends BasePage {

    private By newEntryBy = By.id("novoLancamento");

    private By searchItemBy = By.id("itemBusca");

    private By btnSearchBy = By.id("bth-search");

    private By btnDashboardBy = By.cssSelector("a[title='Dashboard']");

    private By btnRemoveAllBy = By.cssSelector("button[data-bs-target='#modalRemoveAll']");

    private By homeLinkBy = By.id("homeLink");

    private By alertBy = By.cssSelector("div.alert");

    private By alertCloseButtonBy = By.cssSelector("#app > header > nav > div > div.container-fluid > div > div > button");

    private By btnExportBy = By.id("bth-export");

    public EntriesListPage(final WebDriver driver) {
        super(driver);
    }

    public WebElement getNewEntry() {
        return SeleniumUtil.findElementBy(getWebDriver(), newEntryBy);
    }

    public WebElement getSearchItem() {
        return SeleniumUtil.findElementBy(getWebDriver(), searchItemBy);
    }

    public WebElement getBtnSearch() {
        return SeleniumUtil.findElementBy(getWebDriver(), btnSearchBy);
    }

    public WebElement getBtnDashboard() {
        return SeleniumUtil.findElementBy(getWebDriver(), btnDashboardBy);
    }

    public WebElement getBtnRemoveAll() {
        return SeleniumUtil.findElementBy(getWebDriver(), btnRemoveAllBy);
    }

    public WebElement getHomeLink() {
        return SeleniumUtil.findElementBy(getWebDriver(), homeLinkBy);
    }

    public WebElement getAlert() {
        return SeleniumUtil.findElementBy(getWebDriver(), alertBy);
    }

    public WebElement getAlertCloseButton() {
        return SeleniumUtil.findElementBy(getWebDriver(), alertCloseButtonBy);
    }

    public GridUI getGrid(){
        return new GridUI(getWebDriver()).id("tabelaLancamentos");
    }

    @Override
    protected boolean isReady() {
        return getBtnSearch().isDisplayed();
    }
}
