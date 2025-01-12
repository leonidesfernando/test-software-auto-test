package br.com.home.lab.softwaretesting.automation.selenium.webdriver.test;

import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.EntryRecord;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.selenium.webdriver.action.EntriesListAction;
import br.com.home.lab.softwaretesting.automation.util.DataGen;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertTrue;

public class EntriesTest extends BaseSeleniumTest {

    private final Semaphore semaphore = new Semaphore(1);
    private static final List<Category> categories = Arrays.asList(Category.values());
    private static final Map<List<Category>, List<EntryType>> entryTypes;

    static {

        entryTypes = new HashMap<>();
        entryTypes.put(Arrays.asList(Category.INVESTIMENTS, Category.OTHER),
                Collections.singletonList(EntryType.TRANSF));

        entryTypes.put(Arrays.asList(Category.WAGE, Category.OTHER),
                Collections.singletonList(EntryType.INCOME));

        entryTypes.put(Stream.of(Category.values())
                        .filter(c -> c != Category.INVESTIMENTS && c != Category.WAGE)
                        .collect(Collectors.toList()),
                Collections.singletonList(EntryType.EXPENSE));
    }

    private EntriesListAction entriesListAction;
    public static final String ENTRIES = "entries";


    public EntriesTest() {
        super();
    }

    @Step("Initializing the context")
    @BeforeClass
    protected void setUp() {
        context.setContext(ENTRIES, new LinkedBlockingQueue<EntryRecord>());
    }

    @Step("Performing log into with credentials from configurations")
    @Test
    public void loginEntries() {
        super.login();
    }

    @Step("Registering entries by dynamic data")
    @Test(dependsOnMethods = "loginEntries")//, invocationCount = 3)
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

        assertTrue(entriesListAction.checkSuccessfulEntryRegistyMessage());
        assertTrue(entriesListAction.existEntry(description, date, entryType));
        setEntryInContext(new EntryRecord(description, date, entryType));

    }

    @Step("Searching by description from the context")
    @Test(dependsOnMethods = "addEntry")
    void searchForDescription() {
        EntryRecord entryRecord = getEntryInContext();
        entriesListAction.goHome();
        entriesListAction.searchFor(entryRecord.description());
        entriesListAction.buscaLancamentoPorPaginaDescricao(entryRecord.description());
        entriesListAction.checkEntryExists(entryRecord.description(), entryRecord.entryDate(), entryRecord.type());
        setEntryInContext(entryRecord);
    }

    @Issue("It's not searching the item to be edited. It must be fixed")
    @Step("Editing an entry existing in the context")
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
        setEntryInContext(entryRecord);
    }

    @Step("Removing the entry by description collected from the context")
    @Test(dependsOnMethods = "editEntry")
    void removeEntry() {
        EntryRecord entryRecord = getEntryInContext();
        entriesListAction.goHome();
        entriesListAction.removeEntry(entryRecord.description());
    }

    @Step("Performing log out")
    @Test(dependsOnMethods = "removeEntry")
    public void logout() {
        super.doLogout();
    }

    private String getDescription() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter entryFormat = DateTimeFormatter.ofPattern("dd.MM.yy-ss");
        StringJoiner entryDescription = new StringJoiner(" ")
                .add("Launching automated output")
                .add(DataGen.productName())
                .add(dateTime.format(entryFormat));
        return entryDescription.toString();
    }

    private BigDecimal getEntryValue() {

        return BigDecimal.valueOf(DataGen.moneyValue())
                .setScale(2, RoundingMode.HALF_UP);
    }
    private Category getCategory() {
        return getAny(categories);
    }

    private <T> T getAny(List<T> list) {
        int n = list.size();
        int index = DataGen.number(n - 1);
        return list.get(index);
    }

    private EntryRecord getEntryInContext() {
        try {
            semaphore.acquire();
            Queue<EntryRecord> queue = getEntries();
            EntryRecord entryRecord = queue.poll();
            Objects.requireNonNull(entryRecord);
            semaphore.release();
            return entryRecord;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private EntryType getEntryType(Category category) {
        return entryTypes.entrySet().stream()
                .filter(entry -> entry.getKey().contains(category))
                .map(entry -> getAny(entry.getValue()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Does not exists 'EntryType' for this category " + category));
    }

    private void setEntryInContext(EntryRecord entryRecord) {
        try {
            semaphore.acquire();
            Objects.requireNonNull(entryRecord);
            getEntries().add(entryRecord);
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private Queue<EntryRecord> getEntries() {
        return context.get(ENTRIES);
    }
}
