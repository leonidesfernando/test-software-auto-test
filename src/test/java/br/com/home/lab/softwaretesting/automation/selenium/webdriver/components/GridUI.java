package br.com.home.lab.softwaretesting.automation.selenium.webdriver.components;

import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

public class GridUI extends GenericUI{

    private WebElement grid;

    public GridUI(WebDriver webDriver) {
        super(webDriver);
    }


    @Override
    protected boolean isReady() {
        Objects.requireNonNull(grid);
        grid = SeleniumUtil.waitForElementVisible(getWebDriver(), grid);
        return grid.isDisplayed();
    }

    public GridUI gridElement(WebElement grid){
        this.grid = grid;
        return this;
    }

    public GridUI id(String id){
        this.grid = getWebDriver().findElement(By.id(id));
        return this;
    }

    public GridUI xpath(String xpath){
        this.grid = getWebDriver().findElement(By.xpath(xpath));
        return this;
    }

    @Override
    public WebElement getWrappedElement() {
        Objects.requireNonNull(grid);
        return grid;
    }

    public List<WebElement> getElements(){
        if (!areThereElements()) {
            throw new IllegalStateException("No elements present. The data table is empty.");
        }
        final String xpath = ".//tbody/tr";
        SeleniumUtil.waitForPresenceOfXpath(getWebDriver(), xpath);
        return getWebDriver().findElements(By.xpath(xpath));
    }

    public String getCellValueAt(int row, String col){
        return getColumnsAtLine(row).get(getColumnIndex(col)).getText();
    }

    public List<WebElement> getButtonsAt(int row, int col){
        return getColumnsAtLine(row).get(col).findElements(By.tagName("a"));
    }

    public List<WebElement> getColumnsAtLine(int row){
        return getElements().get(row).findElements(By.tagName("td"));
    }

    private int getColumnIndex(String column){
        List<WebElement> header = getHeader();
        for(int i = 0; i < header.size(); i++){
            String columnHeader = header.get(i).getText().trim();
            if(columnHeader.equalsIgnoreCase(column)){
                return i;
            }
        }
        throw new IllegalArgumentException(String.format("The column '%s' doesn't exists on the grid.", column));
    }

    private List<WebElement> getHeader(){
        return getWebDriver().findElements(By.xpath(".//thead/tr/th"));
    }

    public boolean areThereElements(){
        try{
            SeleniumUtil.waitForPresenceOfId(getWebDriver(), grid.getAttribute("id"));
            getWebDriver().findElement(By.className("ui-empty-table"));
            return false;
        }catch (NoSuchElementException e){
            return true;
        }
    }
}
