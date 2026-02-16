package br.com.home.lab.softwaretesting.automation.selenium.webdriver.test;

import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.EntryRecord;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.action.EntriesListAction;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.helper.SeleniumUtil;
import br.com.home.lab.softwaretesting.automation.util.DataGen;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.testng.Tag;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

import static br.com.home.lab.softwaretesting.automation.util.Constants.REGRESSION_TEST;
import static br.com.home.lab.softwaretesting.automation.util.Constants.SMOKE_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

@Epic("Entries test running on Selenium WebDriver")
public class EntriesTest extends BaseSeleniumTest {

    private final Semaphore semaphore = new Semaphore(1);
    private static final List<Category> categories = Arrays.asList(Category.values());
    private static final Map<List<Category>, List<EntryType>> entryTypes;

    static {

        entryTypes = new HashMap<>();
        entryTypes.put(Arrays.asList(Category.INVESTMENTS, Category.OTHER),
                Collections.singletonList(EntryType.TRANSF));

        entryTypes.put(Arrays.asList(Category.WAGE, Category.OTHER),
                Collections.singletonList(EntryType.INCOME));

        entryTypes.put(Stream.of(Category.values())
                        .filter(c -> c != Category.INVESTMENTS && c != Category.WAGE)
                        .toList(),
                Collections.singletonList(EntryType.EXPENSE));
    }

    private EntriesListAction entriesListAction;
    public static final String ENTRIES = "entries";


    public EntriesTest() {
        super();
    }

    @BeforeClass
    protected void setUp() {
        initializeTests();
    }

    @Step("Initializing the context")
    private void initializeTests(){
        context.setContext(ENTRIES, new LinkedBlockingQueue<EntryRecord>());
    }

    @Description("Performing log into with credentials from configurations")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.BLOCKER)
    @Test
    public void loginEntries() {
        super.login();
    }

    @Description("Registering entries by dynamic data on web ui")
    @Severity(SeverityLevel.BLOCKER)
    @Tag(REGRESSION_TEST)
    @Test(dependsOnMethods = "loginEntries", invocationCount = 4)
    public void addEntry() {

        String description = getDescription();
        BigDecimal value = getEntryValue();
        String date = DataGen.strDateCurrentMonthEnglisFormat();
        Category category = getCategory();
        EntryType entryType = getEntryType(category);
        entriesListAction = new EntriesListAction(getWebDriver());
        entriesListAction.newEntry()
                .and()
                .saveEntry(description, value,
                        date, entryType, category);

        assertTrue(entriesListAction.existEntry(description, date, entryType));
        setEntryInContext(new EntryRecord(description, date, entryType, category));
    }

    @Description("Searching by description from the context")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.BLOCKER)
    @Test(dependsOnMethods = "addEntry")
    void searchForDescription() {
        SeleniumUtil.waitTillUrlContains(getWebDriver(), "entries");
        EntryRecord entryRecord = getEntryInContext();
        entriesListAction.goHome();
        entriesListAction.searchFor(entryRecord.description());
        entriesListAction.buscaLancamentoPorPaginaDescricao(entryRecord.description());
        entriesListAction.checkEntryExists(entryRecord.description(), entryRecord.entryDate(), entryRecord.type());
    }

    @Issue("It's not searching the item to be edited. It must be fixed")
    @Description("Editing an entry existing in the context")
    @Tag(REGRESSION_TEST)
    @Tag(SMOKE_TEST)
    @Severity(SeverityLevel.NORMAL)
    @Test(dependsOnMethods = "searchForDescription")
    void editEntry() {
        String editionSuffix = " EDITED by Selenium";
        EntryRecord entryRecord = getEntryInContext();
        final String newDescription = entryRecord.description() + editionSuffix;
        entriesListAction.goHome();
        entriesListAction.searchFor(entryRecord.description());
        entriesListAction.openEntryToEdit()
                .and()
                .setDescription(newDescription)
                .then()
                .saveEntry();

        assertTrue(entriesListAction.existsEntryByDescription(newDescription),
                "Should exits the entry that was edited " + (newDescription));
    }

    @Description("Exporting entries to Excel file")
    @Tag(REGRESSION_TEST)
    @Tag(SMOKE_TEST)
    @Severity(SeverityLevel.NORMAL)
    @Test(dependsOnMethods = "searchForDescription")
    void exportEntriesToExcel() {
        entriesListAction.goHome();
        entriesListAction.clickToExportEntriesToExcel();
        entriesListAction.validateExcelFileHeader();
    }


    @Description("Removing the entry by description collected from the context")
    @Test(dependsOnMethods = "exportEntriesToExcel")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.CRITICAL)
    void removeEntry() {
        EntryRecord entryRecord = getEntryInContext();
        entriesListAction.goHome();
        entriesListAction.removeEntry(entryRecord.description());
    }

    @Description("Not removing all entries from the list")
    @Test(dependsOnMethods = "removeEntry")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.CRITICAL)
    void doNotRemoveAllEntries(){
        entriesListAction.goHome();
        entriesListAction.openModalRemoveAllEntries();
        entriesListAction.doNotRemoveAllEntries();
    }

    @Description("Removing all entries from the list")
    @Test(dependsOnMethods = "doNotRemoveAllEntries")
    @Tag(REGRESSION_TEST)
    @Severity(SeverityLevel.CRITICAL)
    void removeAllEntries(){
        entriesListAction.goHome();
        entriesListAction.openModalRemoveAllEntries();
        entriesListAction.removeAllEntries();
    }

    @Description("Exporting entries to Excel file")
    @Tag(REGRESSION_TEST)
    @Tag(SMOKE_TEST)
    @Severity(SeverityLevel.NORMAL)
    @Test(dependsOnMethods = "removeAllEntries", enabled = false)
    void exportEmptyEntriesToExcel() {
        entriesListAction.goHome();
        entriesListAction.clickToExportEntriesToExcel();
        entriesListAction.validateEmptyExcelFileHeader();
    }


    @Description("Performing log out")
    @Test(dependsOnMethods = "removeAllEntries")
    public void logout() {
        super.doLogout();
    }

    @Step("Getting a new random description")
    private String getDescription() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter entryFormat = DateTimeFormatter.ofPattern("dd.MM.yy-ss");
        StringJoiner entryDescription = new StringJoiner(" ")
                .add("Launching automated output")
                .add(DataGen.productName())
                .add(dateTime.format(entryFormat));
        return entryDescription.toString();
    }

    @Step("Getting a new random value")
    private BigDecimal getEntryValue() {
        return DataGen.amount();
    }

    @Step("Getting a new random category given the available ones")
    private Category getCategory() {
        return getAny(categories);
    }

    private <T> T getAny(List<T> list) {
        int n = list.size();
        if(n == 1) {
            return list.get(0);
        }
        int index = DataGen.number(n-1);
        return list.get(index);
    }

    @Step("Get one entry from the test context")
    private EntryRecord getEntryInContext() {
        try {
            semaphore.acquire();
            Queue<EntryRecord> queue = getEntries();
            assertThat(queue).hasSizeGreaterThan(0);
            EntryRecord entryRecord = queue.peek();
            Objects.requireNonNull(entryRecord);
            semaphore.release();
            return entryRecord;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Step("Get EntryType by Category")
    private EntryType getEntryType(Category category) {
        return entryTypes.entrySet().stream()
                .filter(entry -> entry.getKey().contains(category))
                .map(entry -> getAny(entry.getValue()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Does not exists 'EntryType' for this category " + category));
    }

    @Step("Setting an entry into the test context")
    private void setEntryInContext(EntryRecord entryRecord) {
        try {
            semaphore.acquire();
            Objects.requireNonNull(entryRecord);
            getEntries().add(entryRecord);
            semaphore.release();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private Queue<EntryRecord> getEntries() {
        return context.get(ENTRIES);
    }
}
