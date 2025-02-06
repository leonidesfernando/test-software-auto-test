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
import io.qameta.allure.Step;
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


public class ControllerTest extends AbstractTestNGSpringContextTests {

    private final Semaphore semaphore = new Semaphore(1);
    private static final ScenarioContextData context = new ScenarioContextData();


    @Step("Loading configurations(url and port) and performing log into the system")
    @BeforeTest
    public void before() {
        RestAssuredConfigSetup.configureRestAssured();
        RestAssured.baseURI = LoadConfigurationUtil.getOnlyApiUrl();
        RestAssured.port = LoadConfigurationUtil.getPort();
        context.setContext(ENTRIES, new CopyOnWriteArrayList<Entry>());
        login();
    }

    private void login(){
        User user = LoadConfigurationUtil.getUser();
        String token =  RestAssurredUtil.doLogin(user, "api/auth/signin");
        Assert.assertTrue(Strings.isNotNullAndNotEmpty(token));
        context.setContext(AUTH_TOKEN, token);
    }

    private String getToken(){
        return context.get(AUTH_TOKEN);
    }

    private void addEntryInContext(Entry entryWhitoutId, long id){
        var newEntry = new Entry.EntryBuilder()
                .id(id)
                .description(entryWhitoutId.getDescription())
                .amount(entryWhitoutId.getAmount())
                .category(entryWhitoutId.getCategory())
                .entryType(entryWhitoutId.getEntryType())
                .entryDate(entryWhitoutId.getEntryDate())
                .build();
        List<Entry> entries = context.get(ENTRIES);
        entries.add(newEntry);
    }

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

    private Long getIdFromContext(){
        List<Entry> entries = context.get(ENTRIES);
        int n = entries.size();
        return entries.get(n-1).getId();
    }

    @Description("Registering new entry by dynamic data generation")
    @Test(priority = 0, invocationCount = 4, testName = "Adding new entry: ", dataProvider = "entryData")
    public void addEntryTest(Entry entry) {
        Response response = RestAssurredUtil.post(getToken(),
                "/api/entries/add", entry);
        response.then().assertThat()
                .statusCode(200)
                .body("message", equalTo("entry.added"));

        addEntryInContext(entry, response.jsonPath().getLong("id"));
        logger.info("Entry added: "+ entry);
    }

    @DataProvider
    private Object[][] entryData(){
        return new Object[][]{
                {newValidEntry()}
        };
    }

    @Test
    public void searchEntryTest(){
        List<Entry> list = context.get(ENTRIES);
        int n =  list.size();
        assert n > 0;
        String description = list.get(DataGen.number(0, n-1)).getDescription();
        FormSearch form = new FormSearch(description,true, 1);

        Response response = RestAssurredUtil.post(getToken(), "/api/entries/search", form);

        var result = response.as(ResultRecord.class);
        var lancamentos = result.entries();
        assertEquals(lancamentos.size(), 1, "Searched by: " + description);
    }

    @SuppressWarnings("rawtypes")
    @Description("Opening the entry by ID present in the context")
    @Test
    void getEntryTest() {
        Response response = RestAssurredUtil.get(getToken(), String.format("/api/entries/get/%d", getIdFromContext()));
        var lancamentoRecord = response.as(Entry.class);

        assertNotEquals(lancamentoRecord.getId(), 0);
        assertNotNull(lancamentoRecord.getCategory());
        assertNotNull(lancamentoRecord.getAmount());
        assertTrue(StringUtils.isNotBlank(lancamentoRecord.getDescription()));
        assertNotNull(lancamentoRecord.getEntryDate());
        assertNotNull(lancamentoRecord.getEntryType());
    }

    @Test
    void updateEntryTest(){
        Long id = getIdFromContext();
        Entry entry = EntryDataUtil.getUpdateEntryData(id, newValidEntry());
        var response = RestAssurredUtil.put(getToken(), "/api/entries/update", entry);
        var jsonPath = response.jsonPath();
        assertEquals(jsonPath.getString("message"), "entry.updated");
        assertEquals(jsonPath.getLong("id"), id);
    }

    @Test
    void deleteEntryTest(){
        long id = getIdFromContext();
        var response = RestAssurredUtil.delete(getToken(), String.format("/api/entries/remove/%d", id));
        var jsonPath = response.jsonPath();
        assertEquals(jsonPath.getString("message"), "entry.removed");
        assertEquals(jsonPath.getLong("id"), id);
        removeEntryById(id);
    }

    @Test
    void checkProfile(){
        var response = RestAssurredUtil.get(getToken(), "/api/check/profile");
        JsonPath jsonPath = response.jsonPath();
        jsonPath.getLong("id");
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("username")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("email")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("authorities")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("enabled")));
    }
}
