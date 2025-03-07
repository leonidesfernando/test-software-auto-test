package br.com.home.lab.softwaretesting.automation.cucumber.definitions;


import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.Entry;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.model.User;
import br.com.home.lab.softwaretesting.automation.model.converter.StringToDateConverter;
import br.com.home.lab.softwaretesting.automation.model.record.FormSearch;
import br.com.home.lab.softwaretesting.automation.model.record.MessageResponse;
import br.com.home.lab.softwaretesting.automation.model.record.ResultRecord;
import br.com.home.lab.softwaretesting.automation.model.record.UserProfile;
import br.com.home.lab.softwaretesting.automation.restassured.RestAssurredUtil;
import br.com.home.lab.softwaretesting.automation.util.EntryDataUtil;
import br.com.home.lab.softwaretesting.automation.util.LoadConfigurationUtil;
import br.com.home.lab.softwaretesting.automation.util.ScenarioContextData;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.util.Strings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

import static br.com.home.lab.softwaretesting.automation.util.Constants.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Epic("EntryControllerStepDefinitions - Cucumber tests using API/RestAssured")
public class EntryControllerStepDefinitions {

    private final Semaphore semaphore = new Semaphore(1);
    private static final ScenarioContextData context = new ScenarioContextData();

    private static final User user = LoadConfigurationUtil.getUser();


    @Given("User and password existent in to the settings")
    public void userPasswordExistentSettings() {
        assertNotNull(user);
    }

    @Then("You must log in and access the home page")
    public void youMustLoginAndAccessTheHomePage() {
        String authToken =  RestAssurredUtil.doLogin(user, LOGIN_ENDPOINT);
        Assert.assertTrue(Strings.isNotNullAndNotEmpty(authToken));
        context.setContext(AUTH_TOKEN, authToken);

        Response response = RestAssurredUtil.get(getAuthToken(), "/api/check/profile");
        assertEquals(response.getStatusCode(), 200);
        UserProfile userProfile = RestAssurredUtil.extractDataFromBodyResponse(response, getUserProfileAsTypeReference());
        assertEquals(user.username(), userProfile.username());
    }

    private static TypeReference<UserProfile> getUserProfileAsTypeReference() {
        return new TypeReference<UserProfile>() {};
    }

    private static TypeReference<MessageResponse> getMessageResponseAsTypeReference() {
        return new TypeReference<MessageResponse>() {};
    }

    private static TypeReference<ResultRecord> getResultRecordAsTypeReference() {
        return new TypeReference<ResultRecord>() {};
    }

    private String getAuthToken(){
        return context.get(AUTH_TOKEN);
    }

    @And("Remove the first entry found")
    public void remove_the_first_entry_found() {

        var entry = getFirstEntryFromContextWithId();
        Response response = RestAssurredUtil.delete(getAuthToken(),
                String.format("/api/entries/remove/%d",entry.getId()));
        assertEquals(response.getStatusCode(), 200);
        MessageResponse messageResponse = RestAssurredUtil.extractDataFromBodyResponse(response, getMessageResponseAsTypeReference());
        assertEquals(entry.getId(), messageResponse.id());
        assertEquals(messageResponse.message(), "entry.removed");
    }

    @And("Edit the first entry found")
    public void edit_the_first_entry_found() {
        final Entry entry = getFirstEntryFromContextWithId();
        Response response = RestAssurredUtil.put(getAuthToken(), "/api/entries/update/", entry);
        MessageResponse messageResponse = RestAssurredUtil.extractDataFromBodyResponse(response, getMessageResponseAsTypeReference());
        assertEquals(entry.getId(), messageResponse.id());
        assertEquals(messageResponse.message(), "entry.updated");
    }

    @Given("Search an entry by category {string}")
    public void search_an_entry_by_category(String categoria) {
        searchBy(categoria);
    }


    @DataTableType
    public Entry entries(Map<String,String> row){
        return new Entry.EntryBuilder()
                .description(row.get(DESCRIPTION))
                .amount(getAmountByParam(row.get(AMOUNT)))
                .entryType(EntryType.valueOf(row.get(ENTRY_TYPE)))
                .entryDate(getEntryDateByParam(row.get(ENTRY_DATE)))
                .category(Category.valueOf(row.get(CATEGORY)))
                .build();
    }


    private BigDecimal getAmountByParam(String param){
        try{
            final BigDecimal valor = new BigDecimal(NumberFormat
                    .getCurrencyInstance(BR_Locale)
                    .parse(EntryDataTableValues.from(param).value())
                    .toString());
            return valor.setScale(2, RoundingMode.HALF_UP);
        }catch (Exception e){
            return new BigDecimal(param);
        }
    }

    private Date getEntryDateByParam(String param){
        StringToDateConverter converter = new StringToDateConverter();
        try {
            return converter.convert(EntryDataTableValues.from(param).value());
        }catch (IllegalArgumentException e){
            return converter.convert(param);
        }
    }


    @Given("Register entry whit provided data")
    public void register_entry_whit_provided_data(List<Entry> entries) {

        for (Entry entry : entries) {
            Response response = RestAssurredUtil.post(getAuthToken(),
                    ADD_ENDPOPINT, entry);
            var id = extractEntryIdFromResponse(response);
            final Entry newEntry = EntryDataUtil.getUpdateEntryData(id, entry);
            addEntryToContext(newEntry);
        }
    }

    private Response searchBy(String item) {
        FormSearch buscaForm = new FormSearch(item, true, 10);
        Response response = RestAssurredUtil.
                post(getAuthToken(), SEARCH_ENDPOINT, buscaForm);
        List<Entry> list = RestAssurredUtil
                .extractDataFromBodyResponse(response, getResultRecordAsTypeReference())
                .entries();
        list.forEach(this::addEntryToContext);
        return response;
    }

    private void addEntryToContext(Entry entry) {
        try {
            semaphore.acquire();
            List<Entry> entries;
            if (!context.exists(ENTRIES)) {
                context.setContext(ENTRIES, new ArrayList<Entry>());
            }
            entries = context.get(ENTRIES);

            Optional<Entry> opt = entries.stream()
                    .filter(l -> l.getAmount().equals(entry.getAmount()))
                    .filter(l -> l.getEntryType().equals(entry.getEntryType()))
                    .findFirst();
            opt.ifPresent(entries::remove);
            entries.add(entry);
            semaphore.release();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

    }

    private long extractEntryIdFromResponse(Response response){
        return RestAssurredUtil.
                extractDataFromBodyResponse(response, getMessageResponseAsTypeReference())
                .id();
    }

    private synchronized Entry getFirstEntryFromContextWithId() {
        try {
            semaphore.acquire();
            List<Entry> entries = context.get(ENTRIES);
            Optional<Entry> lancamentoRecordOpt = entries.stream().filter(l -> l.getId() != 0L).findFirst();
            Entry entryRecord = lancamentoRecordOpt.orElseThrow(() -> new IllegalStateException("An entry should be found:"));
            entries.remove(entryRecord);
            semaphore.release();
            return entryRecord;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
