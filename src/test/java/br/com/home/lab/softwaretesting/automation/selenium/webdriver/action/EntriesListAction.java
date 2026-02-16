package br.com.home.lab.softwaretesting.automation.selenium.webdriver.action;

import br.com.home.lab.softwaretesting.automation.config.Bundles;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.components.GridUI;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.EntriesListPage;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.RemoveAllModalPage;
import br.com.home.lab.softwaretesting.automation.util.ExcelUtil;
import br.com.home.lab.softwaretesting.automation.util.StringUtil;
import br.com.home.lab.softwaretesting.automation.util.Util;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.time.Duration;

import static br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil.waitForElementInvisible;
import static br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil.waitForElementVisible;
import static br.com.home.lab.softwaretesting.automation.util.Constants.DD_MM_YYYY_SLASH;
import static br.com.home.lab.softwaretesting.automation.util.Constants.YYYY_MMM_DD_DASH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.Assert.*;

public class EntriesListAction extends BaseAction<EntriesListPage>{

    private static final String COL_DESCRIPTION = "description";
    private static final String COL_RELEASE_DATE = "entry.date";
    private static final String COL_TYPE = "entry.type";

    enum Button {
        EDIT,
        REMOVE
    }

    public EntriesListAction(WebDriver webDriver) {
        super(webDriver, new EntriesListPage(webDriver));
    }

    @Step("Going to the home page")
    public EntriesListAction goHome() {
        page.get();
        SeleniumUtil.jsClick(getWebDriver(), page.getHomeLinkBy());
        SeleniumUtil.waitAjaxCompleted(getWebDriver());
        WebElement btnNew = SeleniumUtil.waitForElementVisible(getWebDriver(), page.getNewEntryBy());
        assertThat(btnNew)
                .isNotNull();
        assertThat(btnNew.isDisplayed()).isTrue();
        return this;
    }

    @Step("Clicking on the button to create a new entry")
    public EntryAction newEntry() {
        page.get();
        page.getNewEntry().click();
        return new EntryAction(getWebDriver());
    }

    @Step("Opening the desired entry for edition")
    public EntryAction openEntryToEdit() {
        page.get();
        return clicaBotaoEditar();
    }

    @Step("Searching by: ${texto}")
    public void searchFor(String texto) {
        buscaLancamentoPorDescricao(texto);
        GridUI grid = page.getGrid();
        assertFalse(grid.getElements().isEmpty());
    }

    public boolean existEntry(final String entryDescription, String date, EntryType type) {
        buscaLancamentoPorDescricao(entryDescription);
        checkEntryExists(entryDescription, date, type);
        return true;
    }

    @Step("Checking if the entry with description: '${entryDescription}', date: '${date}' and type: '${tipo}' exists")
    public void checkEntryExists(String entryDescription, String date, EntryType tipo) {
        GridUI grid = page.getGrid();
        assertEquals(grid.getElements().size(), 1);
        final var gridDescription = grid.getCellValueAt(0, getMessageByKey(COL_DESCRIPTION));
        assertTrue(gridDescription.contains(entryDescription));
        assertEquals(grid.getCellValueAt(0, getMessageByKey(COL_RELEASE_DATE)), StringUtil.stringDateAsFormat(date, YYYY_MMM_DD_DASH, DD_MM_YYYY_SLASH));
        assertEquals(grid.getCellValueAt(0, getMessageByKey(COL_TYPE)), tipo.getDescription(getLanguage()));

    }

    @Step("Removing the entry with the description ${descricaoLancamento}")
    public void removeEntry(final String descricaoLancamento) {
        buscaLancamentoPorDescricao(descricaoLancamento);
        clicaBotaoExcluir();
        buscaLancamentoPorDescricao(descricaoLancamento);
        GridUI grid = page.getGrid();
        assertFalse(grid.areThereElements());
    }

    @Step("Checking if the description '${descricaoLancamento}' exists")
    public boolean existsEntryByDescription(String descricaoLancamento){
        buscaLancamentoPorDescricao(descricaoLancamento);
        GridUI grid = page.getGrid();
        assertEquals(grid.getElements().size(), 1);
        return grid.getCellValueAt(0, getMessageByKey(COL_DESCRIPTION)).equals(descricaoLancamento);
    }

    private void clicaBotao(Button btn){
        GridUI gridUI = page.getGrid();
        gridUI.getButtonsAt(0, 5).get(btn.ordinal()).click();
    }

    @Step("Clicking to edit the desired entry")
    protected EntryAction clicaBotaoEditar(){
        clicaBotao(Button.EDIT);
        return new EntryAction(getWebDriver());
    }

    @Step("Clicking to remove a desired entry")
    protected void clicaBotaoExcluir() {
        clicaBotao(Button.REMOVE);
    }

    @Step("Searching the entry by description")
    public void buscaLancamentoPorDescricao(String descricaoLancamento) {
        page.get();
        waitForElementVisible(getWebDriver(), page.getSearchItem()).clear();
        waitForElementVisible(getWebDriver(), page.getSearchItem()).sendKeys(descricaoLancamento);
        SeleniumUtil.jsClick(getWebDriver(), page.getBtnSearchBy());
        SeleniumUtil.waitAjaxCompleted(getWebDriver());

    }

    @Step("Searching entry by description with pagination (keeping the pagination of the item searched)")
    public void buscaLancamentoPorPaginaDescricao(String descricaoLancamento) {
        page.get();
        waitForElementVisible(getWebDriver(), page.getSearchItem()).clear();
        waitForElementVisible(getWebDriver(), page.getSearchItem()).sendKeys(descricaoLancamento);
    }

    @Step("Accessing the Dashboard page by Listing entries page using the button")
    public void gotToDashboard() {
        page.getBtnDashboard().click();
        SeleniumUtil.waitTillUrlContains(getWebDriver(), "dashboard");
        DashboardAction dashboardAction = new DashboardAction(getWebDriver());
        dashboardAction.getPage().get();
        dashboardAction.validateDashboardPageIsLoaded();

    }

    public void openModalRemoveAllEntries() {
        page.get();
        waitForElementVisible(getWebDriver(), page.getBtnRemoveAll());
        page.getBtnRemoveAll().click();
        RemoveAllModalPage removeAllModalPage = new RemoveAllModalPage(getWebDriver());
        waitForElementVisible(getWebDriver(), removeAllModalPage.getBtnYesRemoveAllBy());
    }

    @Step("Removing all entries, opening the modal, confirming and managing AJAX")
    public boolean removeAllEntries() {
        page.get();
        RemoveAllModalAction removeAllModalAction = new RemoveAllModalAction(getWebDriver());
        removeAllModalAction.removeAllEntries();
        return checkListingIsEmpty();
    }

    @Step("Not Remove all entries, opening the modal, does not confirm and managing AJAX")
    public boolean doNotRemoveAllEntries() {
        page.get();
        RemoveAllModalAction removeAllModalAction = new RemoveAllModalAction(getWebDriver());
        removeAllModalAction.doNotRemoveAllEntries();
        return checkListingIsNotEmpty();
    }

    @Step("Checking if the listing is empty")
    public boolean checkListingIsEmpty() {
        GridUI grid = page.getGrid();
        assertFalse(grid.areThereElements());
        return true;
    }

    @Step("Checking if the listing is NOT empty")
    public boolean checkListingIsNotEmpty() {
        GridUI grid = page.getGrid();
        assertThat(grid.areThereElements()).isTrue();
        return true;
    }

    @Step("Checking the successful entry registry message")
    public void checkSuccessfulEntryRegistyMessage(){
        final String message = page.getAlert().getText();
        final var expected = Util.getMessageByKey("entry.added");
        assertThat(message).containsPattern(expected);
    }

    @Step("Closing the entry registry success alert")
    public void closeEntryRegistrySuccessAlert(){
        SeleniumUtil.jsClick(getWebDriver(), page.getAlertCloseButtonBy());
        waitForElementInvisible(getWebDriver(), page.getAlertCloseButtonBy());
    }

    @Step("Exporting entries to Excel")
    public void clickToExportEntriesToExcel() {
        SeleniumUtil.waitForElementVisible(getWebDriver(), page.getBtnExportBy());
        SeleniumUtil.jsClick(getWebDriver(), page.getBtnExportBy());
        SeleniumUtil.waitAjaxCompleted(getWebDriver());
    }

    @Step("Validating the exported Excel file header content")
    public void validateExcelFileHeader() {
        try {
            ExcelUtil.validateExcelFileHeader(SeleniumUtil.waitForExcelFile(Duration.ofSeconds(30)));
        }catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException("Fail to read excel file",e);
        }
    }

    @Step("Validating the exported Excel file is empty")
    public void validateEmptyExcelFileHeader() {
        try{
            ExcelUtil.validateEmptyExcelFileHeader(SeleniumUtil.waitForExcelFile(Duration.ofSeconds(30)));
        }catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException("Fail to read excel file",e);
        }
    }

    @Override
    public EntriesListPage getPage() {
        return page;
    }

    private String getMessageByKey(String key){
        return Bundles.getMessage(key, getLanguage());
    }
}
