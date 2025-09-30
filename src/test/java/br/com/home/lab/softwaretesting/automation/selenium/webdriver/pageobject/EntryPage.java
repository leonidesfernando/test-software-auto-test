package br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject;

import br.com.home.lab.softwaretesting.automation.model.EntryType;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;


public class EntryPage extends BasePage {

    @CacheLookup @FindBy(css = "label[for='INCOME']")
    private WebElement income;

    @CacheLookup @FindBy(css = "label[for='EXPENSE']")
    private WebElement expense;

    @CacheLookup @FindBy(css = "label[for='TRANSF']")
    private WebElement transf;

    @CacheLookup @Getter @FindBy(id = "description")
    private WebElement description;

    @CacheLookup @Getter @FindBy(name = "entryDate")
    private WebElement entryDate;

    @CacheLookup @Getter @FindBy(id = "amount")
    private WebElement amount;

    @CacheLookup @FindBy(id = "category")
    private WebElement categoryWebElement;

    @CacheLookup @Getter @FindBy(id = "btnSalvar")
    private WebElement btnSave;


    public EntryPage(final WebDriver driver){
        super(driver);
    }

    public Select getCategoryCombo(){
        return new Select(categoryWebElement);
    }

    public WebElement getDivError(){
        return getWebDriver().findElement(By.cssSelector("div.alert.alert-danger"));
    }

    public void setEntryType(EntryType entryType){
        switch (entryType){
            case EXPENSE -> {
                expense.click();
                expense.click();
            }
            case INCOME -> {
                income.click();
                income.click();
            }
            case TRANSF -> {
                transf.click();
                transf.click();
            }
        }
    }

    @Override
    protected boolean isReady() {
        return income.isEnabled() &&
                expense.isEnabled() &&
                transf.isEnabled() &&
                btnSave.isEnabled();
    }
}
