package br.com.home.lab.softwaretesting.automation.restassured;

import br.com.home.lab.softwaretesting.automation.model.Entry;
import br.com.home.lab.softwaretesting.automation.model.User;
import br.com.home.lab.softwaretesting.automation.model.record.FormSearch;
import br.com.home.lab.softwaretesting.automation.model.record.ResultRecord;
import br.com.home.lab.softwaretesting.automation.util.DataGen;
import br.com.home.lab.softwaretesting.automation.util.EntryDataUtil;
import br.com.home.lab.softwaretesting.automation.util.LoadConfigurationUtil;
import br.com.home.lab.softwaretesting.automation.util.ScenarioContextData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.testng.Tag;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

import static br.com.home.lab.softwaretesting.automation.util.Constants.*;
import static br.com.home.lab.softwaretesting.automation.util.EntryDataUtil.newValidEntry;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.*;


@Epic("ControllerTest - Testing API by RestAssured")
public class ControllerTest extends AbstractTestNGSpringContextTests {

    private final Semaphore semaphore = new Semaphore(1);
    private static final ScenarioContextData context = new ScenarioContextData();


    @BeforeTest
    public void before() {
        initializeTest();
        login();
    }

    @Step("Loading configurations(url and port) and performing log into the system")
    private void initializeTest(){
        RestAssuredConfigSetup.configureRestAssured();
        RestAssured.baseURI = LoadConfigurationUtil.getOnlyApiUrl();
        RestAssured.port = LoadConfigurationUtil.getPort();
        context.setContext(ENTRIES, new CopyOnWriteArrayList<Entry>());
    }

    @Step("Perform login using user from configuration and store auth token in the context")
    private void login(){
        User user = LoadConfigurationUtil.getUser();
        String token =  RestAssurredUtil.doLogin(user, LOGIN_ENDPOINT);
        Assert.assertTrue(Strings.isNotNullAndNotEmpty(token));
        context.setContext(AUTH_TOKEN, token);
    }

    @Feature("Add new entry")
    @Severity(SeverityLevel.BLOCKER)
    @Tag(REGRESSION_TEST)
    @Test(priority = 0, invocationCount = 4, testName = "Adding new entry: ", dataProvider = "entryData")
    public void addEntryTest(Entry entry) {
        Response response = RestAssurredUtil.post(getToken(),
                ADD_ENDPOPINT, entry);
        response.then().assertThat()
                .statusCode(200)
                .body("message", equalTo("entry.added"));

        addEntryInContext(entry, response.jsonPath().getLong("id"));
        logger.info("Entry added: "+ entry);
    }

    @Description("Search entry using entries add previously")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(REGRESSION_TEST)
    @Test
    public void searchEntryTest(){
        List<Entry> list = context.get(ENTRIES);
        int n =  list.size();
        assert n > 0;
        String description = list.get(DataGen.number(0, n-1)).getDescription();
        FormSearch form = new FormSearch(description,true, 1);

        Response response = RestAssurredUtil.post(getToken(), SEARCH_ENDPOINT, form);

        var result = response.as(ResultRecord.class);
        var lancamentos = result.entries();
        assertEquals(lancamentos.size(), 1, "Searched by: " + description);
    }

    @Description("Getting an entry added previously from the test context")
    @Severity(SeverityLevel.CRITICAL)
    @Tag(REGRESSION_TEST)
    @Tag(SMOKE_TEST)
    @Test
    void getEntryTest() {
        Response response = RestAssurredUtil.get(getToken(), String.format(GET_ENDPOINT, getIdFromContext()));
        var lancamentoRecord = response.as(Entry.class);

        assertNotEquals(lancamentoRecord.getId(), 0);
        assertNotNull(lancamentoRecord.getCategory());
        assertNotNull(lancamentoRecord.getAmount());
        assertTrue(StringUtils.isNotBlank(lancamentoRecord.getDescription()));
        assertNotNull(lancamentoRecord.getEntryDate());
        assertNotNull(lancamentoRecord.getEntryType());
    }

    @Description("Updating an existing entry")
    @Severity(SeverityLevel.NORMAL)
    @Tag(REGRESSION_TEST)
    @Test
    void updateEntryTest(){
        Long id = getIdFromContext();
        Entry entry = EntryDataUtil.getUpdateEntryData(id, newValidEntry());
        var response = RestAssurredUtil.put(getToken(), UPDATE_ENDPOINT, entry);
        var jsonPath = response.jsonPath();
        assertEquals(jsonPath.getString("message"), "entry.updated");
        assertEquals(jsonPath.getLong("id"), id);
    }

    @Description("Deleting an entry")
    @Severity(SeverityLevel.NORMAL)
    @Tag(REGRESSION_TEST)
    @Test
    void deleteEntryTest(){
        long id = getIdFromContext();
        var response = RestAssurredUtil.delete(getToken(), String.format(REMOVE_ENDPOINT, id));
        var jsonPath = response.jsonPath();
        assertEquals(jsonPath.getString("message"), "entry.removed");
        assertEquals(jsonPath.getLong("id"), id);
        removeEntryById(id);
    }


    @Feature("Checking profile of the logged user")
    @Description("Checking profile")
    @Severity(SeverityLevel.MINOR)
    @Tag(REGRESSION_TEST)
    @Test
    void checkProfile(){
        var response = RestAssurredUtil.get(getToken(), CHECK_PROFILE_ENDPOINT);
        JsonPath jsonPath = response.jsonPath();
        jsonPath.getLong("id");
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("username")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("email")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("authorities")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("enabled")));
    }

    @DataProvider
    private Object[][] entryData(){
        return new Object[][]{
                {newValidEntry()}
        };
    }

    @Step("Getting token from the context")
    private String getToken(){
        return context.get(AUTH_TOKEN);
    }

    @Step("Adding entry into the test context")
    private void addEntryInContext(Entry entryWithoutId, long id){
        var newEntry = new Entry.EntryBuilder()
                .id(id)
                .description(entryWithoutId.getDescription())
                .amount(entryWithoutId.getAmount())
                .category(entryWithoutId.getCategory())
                .entryType(entryWithoutId.getEntryType())
                .entryDate(entryWithoutId.getEntryDate())
                .build();
        List<Entry> entries = context.get(ENTRIES);
        entries.add(newEntry);
    }

    @Step("Removing entry form the test context by ID")
    private void removeEntryById(long id){
        try {
            semaphore.acquire();
            List<Entry> entries = context.get(ENTRIES);
            var entry = entries.stream()
                    .filter(e -> e.getId() == id).findFirst()
                    .orElseThrow(IllegalStateException::new);
            entries.remove(entry);
            semaphore.release();
        }catch (InterruptedException e){
            logger.error(e.getMessage());
            throw new IllegalStateException("Failure to remove saved entry from List",e);
        }
    }

    @Step("Get entryId from the test context")
    private Long getIdFromContext(){
        List<Entry> entries = context.get(ENTRIES);
        int n = entries.size();
        return entries.get(n-1).getId();
    }
}
