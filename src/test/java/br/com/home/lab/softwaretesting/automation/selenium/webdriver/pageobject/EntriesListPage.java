package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.components.GridUI;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

@Getter
public class EntriesListPage extends BasePage {

    @FindBy(id = "novoLancamento")
    private WebElement newEntry;

    @FindBy(id = "itemBusca")
    private WebElement searchItem;

    @FindBy(id = "bth-search")
    private WebElement btnSearch;

    @FindBy(css = "a[title='Dashboard']")
    private WebElement btnDashboard;

    //@FindBy(id = "pagina1")
    //private WebElement firstPaginationLink;

    @CacheLookup
    @FindBy(css = "button[data-bs-target='#modalRemoveAll']")
    private WebElement btnRemoveAll;

    @CacheLookup
    @FindBy(id = "modalRemoveAll")
    private WebElement modalRemoveAll;

    @CacheLookup
    @FindBy(id = "btnYesRemoveAll")
    private WebElement btnYesRemoveAll;

    @CacheLookup
    @FindBy(id = "overlayRemoveAll")
    private WebElement waitRemoveAll;

    @FindBy(id = "homeLink")
    private WebElement homeLink;

    @FindBy(xpath = "//div[@class]/span")
    private WebElement alert;
    
    private static final String LIST_TABLE_ID = "divTabelaLancamentos";

    public EntriesListPage(final WebDriver driver) {
        super(driver);
    }

    public GridUI getGrid(){
        return new GridUI(getWebDriver()).id("tabelaLancamentos");
    }

    @Override
    protected boolean isReady() {
        return SeleniumUtil.waitForElementVisible(getWebDriver(), btnSearch).isEnabled()
                && homeLink.isDisplayed() && homeLink.isEnabled();
    }
}
