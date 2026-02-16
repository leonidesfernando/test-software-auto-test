package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class EntryPage extends BasePage {

    private final By incomeBy = By.cssSelector("label[for='INCOME']");

    private final By expenseBy = By.cssSelector("label[for='EXPENSE']");

    private final By transfBy = By.cssSelector("label[for='TRANSF']");

    private final By descriptionBy = By.id("description");

    private final By entryDateBy = By.name("entryDate");

    private final By amountBy = By.id("amount");

    private final By categoryBy = By.id("category");

    @Getter
    private final By btnSaveBy = By.id("btnSalvar");


    public EntryPage(final WebDriver driver){
        super(driver);
    }

    public Select getCategoryCombo(){
        return new Select(getCatetoryWebElement());
    }

    public WebElement getDivError(){
        return getWebDriver().findElement(By.cssSelector("div.alert.alert-danger"));
    }

    public WebElement getCatetoryWebElement() {
        return getWebDriver().findElement(categoryBy);
    }

    public void setEntryType(EntryType entryType){
        switch (entryType){
            case EXPENSE -> {
                getExpense().click();
                getExpense().click();
            }
            case INCOME -> {
                getIncome().click();
                getIncome().click();
            }
            case TRANSF -> {
                getTransf().click();
                getTransf().click();
            }
        }
    }

    @Override
    protected boolean isReady() {
        return getIncome().isEnabled() &&
                getExpense().isEnabled() &&
                getTransf().isEnabled() &&
                getBtnSave().isEnabled();
    }

    public WebElement getIncome() {
        return SeleniumUtil.findElementBy(getWebDriver(), incomeBy);
    }
    public WebElement getExpense() {
        return SeleniumUtil.findElementBy(getWebDriver(), expenseBy);
    }
    public WebElement getTransf() {
        return SeleniumUtil.findElementBy(getWebDriver(), transfBy);
    }
    public WebElement getBtnSave() {
        return SeleniumUtil.findElementBy(getWebDriver(), btnSaveBy);
    }
    public WebElement getDescription() {
        return SeleniumUtil.findElementBy(getWebDriver(), descriptionBy);
    }
    public WebElement getEntryDate() {
        return SeleniumUtil.findElementBy(getWebDriver(), entryDateBy);
    }
    public WebElement getAmount() {
        return SeleniumUtil.findElementBy(getWebDriver(), amountBy);
    }
}
