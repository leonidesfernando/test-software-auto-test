package br.com.home.lab.softwaretesting.automation.selenium.webdriver.action;

import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.EntryPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.math.BigDecimal;

@Slf4j
public class EntryAction extends BaseAction<EntryPage> {


    public EntryAction(WebDriver webDriver) {
        super(webDriver, new EntryPage(webDriver));
    }

    @Override
    public EntryPage getPage() {
        return page;
    }

    public EntryAction and() {
        return this;
    }

    public EntryAction then() {
        return this;
    }

    public void saveEntry(){
        getPage().getBtnSave().click();
        try{
            Assert.fail(String.format(
                    "Houve ao salvar o lancamento. provavelmente um campo nao foi preeenchido. %s",
                    getPage().getDivError().getText())
            );
        }catch (NoSuchElementException e){
            log.info("Entry save successfully");
        }
    }

    public void saveEntry(String entryDescription, BigDecimal value,
                          String date, EntryType type, Category category){
        fillData(entryDescription, value, date, type, category);
        saveEntry();
    }

    public EntryAction setDescription(String entryDescription){
        getPage().getDescription().clear();
        getPage().getDescription().click();
        getPage().getDescription().sendKeys(entryDescription);
        return this;
    }

    private void fillData(String entryDescription, BigDecimal entryAmount,
                          String date, EntryType entryType, Category category){

        getPage().setEntryType(entryType);
        setDescription(entryDescription);

        getPage().getEntryDate().click();
        getPage().getEntryDate().clear();
        SeleniumUtil.fillOutInputWithJS(getWebDriver(), getPage().getEntryDate(), date);

        getPage().getAmount().click();
        getPage().getAmount().clear();
        getPage().getAmount().sendKeys(String.valueOf(entryAmount));
        getPage().getCategoryCombo().selectByValue(category.name());
    }
}
