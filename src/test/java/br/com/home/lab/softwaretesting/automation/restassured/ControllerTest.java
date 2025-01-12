package br.com.home.lab.softwaretesting.automation.restassured;

import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.Entry;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.model.User;
import br.com.home.lab.softwaretesting.automation.model.converter.DateToStringConverter;
import br.com.home.lab.softwaretesting.automation.model.converter.MoneyToStringConverter;
import br.com.home.lab.softwaretesting.automation.model.record.EntryRecord;
import br.com.home.lab.softwaretesting.automation.model.record.FormSearch;
import br.com.home.lab.softwaretesting.automation.model.record.ResultRecord;
import br.com.home.lab.softwaretesting.automation.util.DataGen;
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
import org.testng.annotations.Test;
import org.testng.util.Strings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CopyOnWriteArrayList;

import static br.com.home.lab.softwaretesting.automation.util.Constants.*;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.*;


public class ControllerTest extends AbstractTestNGSpringContextTests {

    private static final ScenarioContextData context = new ScenarioContextData();

    private final EntryType[] entryTypes = EntryType.values();

    private final Category[] categories = Category.values();

    @Step("Loading configurations(url and port) and performing log into the system")
    @BeforeTest
    public void before() {
        RestAssuredConfigSetup.configureRestAssured();
        RestAssured.baseURI = LoadConfigurationUtil.getOnlyUrl();
        RestAssured.port = LoadConfigurationUtil.getPort();
        context.setContext(ENTRIES_DESCRIPTION, new CopyOnWriteArrayList<>());
        context.setContext(ENTRIES_IDS, new CopyOnWriteArrayList<>());
        login();
    }

    private void login(){
        User user = LoadConfigurationUtil.getUser();
        String token =  RestAssurredUtil.doLogin(user, "api/auth/signin");
        Assert.assertTrue(Strings.isNotNullAndNotEmpty(token));
        context.setContext(TOKEN, token);
    }

    private String getToken(){
        return context.get(TOKEN);
    }

    private void addDescriptionInContext(String description){
        List<String> descriptions = context.get(ENTRIES_DESCRIPTION);
        descriptions.add(description);
    }

    private void addEntryIdInContext(long id){
        List<Long> ids = context.get(ENTRIES_IDS);
        ids.add(id);
    }

    private void removeEntryIdContext(long id){
        List<Long> ids = context.get(ENTRIES_IDS);
        ids.remove(id);
    }

    private Long getIdFromContext(){
        List<Long> ids = context.get(ENTRIES_IDS);
        int n = ids.size();
        return ids.get(n-1);
    }

    @Description("Registering new entry by dynamic data generation")
    @Test(priority = 0, invocationCount = 3)
    public void addEntryTest() {
        var lancamento = newEntryTest();
        Response response = RestAssurredUtil.post(getToken(),
                "/api/entries/add", lancamento);
        response.then().assertThat()
                .statusCode(200)
                .body("message", equalTo("entry.added"));

        addDescriptionInContext(lancamento.getDescription());
        addEntryIdInContext(response.jsonPath().getLong("id"));
    }

    private Entry newEntryTest(){
        String descricao = new StringJoiner(" ")
                .add("Assured Rest")
                .add(DataGen.productName())
                .add(LocalDateTime.now().toString()).toString();

        Entry entry = new Entry();
        entry.setDescription(descricao);
        entry.setAmount(BigDecimal.valueOf(DataGen.moneyValue()));
        entry.setEntryDate(DataGen.dateCurrentMonth());
        entry.setEntryType(entryTypes[DataGen.number(0, entryTypes.length-1)]);
        entry.setCategory(categories[DataGen.number(0, categories.length-1)]);
        return entry;
    }

    private EntryRecord getUpdateEntryData(long id, Entry entry){
        MoneyToStringConverter moneyToStringConverter = new MoneyToStringConverter();
        DateToStringConverter dateToStringConverter = new DateToStringConverter();
        return new EntryRecord(id, entry.getDescription(), moneyToStringConverter.convert(entry.getAmount()),
                dateToStringConverter.convert(entry.getEntryDate()), entry.getEntryType().getKey(), entry.getCategory().getNome());
    }

    @Test
    public void searchEntryTest(){
        List<String> list = context.get(ENTRIES_DESCRIPTION);
        int n =  list.size();

        String description = list.get(DataGen.number(0, n-1));
        FormSearch form = new FormSearch(description,true, 1);

        Response response = RestAssurredUtil.post(getToken(), "/api/entries/search", form);

        var result = response.as(ResultRecord.class);
        var lancamentos = result.entries();
        assertEquals(lancamentos.size(), 1);
    }

    @SuppressWarnings("rawtypes")
    @Description("Opening the entry by ID present in the context")
    @Test
    void getEntryTest() {
        Response response = RestAssurredUtil.get(getToken(), String.format("/api/entries/get/%d", getIdFromContext()));
        var lancamentoRecord = response.as(EntryRecord.class);

        assertNotEquals(lancamentoRecord.id(), 0);
        assertNotNull(lancamentoRecord.category());
        assertTrue(StringUtils.isNotBlank(lancamentoRecord.amount()));
        assertTrue(StringUtils.isNotBlank(lancamentoRecord.description()));
        assertTrue(StringUtils.isNotBlank(lancamentoRecord.entryDate()));
        assertNotNull(lancamentoRecord.entryType());
    }

    @Test
    void updateEntryTest(){
        Long id = getIdFromContext();
        EntryRecord lancamentoRecord = getUpdateEntryData(id, newEntryTest());
        var response = RestAssurredUtil.put(getToken(), "/api/entries/update", lancamentoRecord);
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
        removeEntryIdContext(id);
    }

    @Test
    void checkProfile(){
        var response = RestAssurredUtil.get(getToken(), "/api/check/profile");
        JsonPath jsonPath = response.jsonPath();
        assertNotNull(jsonPath.getLong("id"));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("username")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("email")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("authorities")));
        assertTrue(StringUtils.isNotBlank(jsonPath.getString("enabled")));
    }


}
