package br.com.home.lab.softwaretesting.automation.selenium.webdriver.action;

import br.com.home.lab.softwaretesting.automation.config.Bundles;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.components.GridUI;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.pageobject.EntriesListPage;
import br.com.home.lab.softwaretesting.automation.util.StringUtil;
import br.com.home.lab.softwaretesting.automation.util.Util;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil.waitForElementInvisible;
import static br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil.waitForElementVisible;
import static br.com.home.lab.softwaretesting.automation.util.Constants.DD_MM_YYYY_SLASH;
import static br.com.home.lab.softwaretesting.automation.util.Constants.YYYY_MMM_DD_DASH;
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
        page.getHomeLink().click();
        SeleniumUtil.waitAjaxCompleted(getWebDriver());
        SeleniumUtil.waitForElementVisible(getWebDriver(), page.getHomeLink());
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

    private String getMessageByKey(String key){
        return Bundles.getMessage(key, getLanguage());
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
        waitForElementVisible(getWebDriver(), page.getBtnSearch()).click();
    }

    @Step("Searching entry by description with pagination (keeping the pagination of the item searched)")
    public void buscaLancamentoPorPaginaDescricao(String descricaoLancamento) {
        page.get();
        waitForElementVisible(getWebDriver(), page.getSearchItem()).clear();
        waitForElementVisible(getWebDriver(), page.getSearchItem()).sendKeys(descricaoLancamento);
        //waitForElementVisible(getWebDriver(), page.getFirstPaginationLink()).click();
    }

    @Step("Accessing the Dashboard page by Listing entries page using the button")
    public void gotToDashboard() {
        page.getBtnDashboard().click();
        //TODO: add assertion
    }


    @Step("Removing all entries, opening the modal, confirming and managing AJAX")
    public boolean removingAllEntries() {
        page.get();
        page.getBtnRemoveAll().click();
        waitForElementVisible(getWebDriver(), page.getModalRemoveAll());
        page.getBtnYesRemoveAll().click();
        waitForElementInvisible(getWebDriver(), page.getModalRemoveAll());
        return checkListingIsEmpty();
    }

    @Step("Checking if the listing is empty")
    public boolean checkListingIsEmpty() {
        GridUI grid = page.getGrid();
        assertFalse(grid.areThereElements());
        return true;
    }

    @Step("Checking the successful entry registry message")
    public boolean checkSuccessfulEntryRegistyMessage(){
        final String message = page.getAlert().getText();
        return message.equals(Util.getMessageByKey("entry.added"));
    }

    @Override
    public EntriesListPage getPage() {
        return page;
    }
}
