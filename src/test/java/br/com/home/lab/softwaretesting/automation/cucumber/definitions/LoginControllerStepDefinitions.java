package br.com.home.lab.softwaretesting.automation.cucumber.definitions;

import br.com.home.lab.softwaretesting.automation.model.User;
import br.com.home.lab.softwaretesting.automation.model.record.LoginCredentialRecord;
import br.com.home.lab.softwaretesting.automation.restassured.RestAssurredUtil;
import br.com.home.lab.softwaretesting.automation.util.LoadConfigurationUtil;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import org.testng.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginControllerStepDefinitions {

    private static final User user = LoadConfigurationUtil.getUser();
    private List<Exception> exceptions = new ArrayList<>();

    @Given("The following credentials then")
    public void the_following_credentials_then(List<LoginCredentialRecord> credentials){

        for (LoginCredentialRecord credential : credentials) {
            var user = getUserByCredential(credential);
            String token =  RestAssurredUtil.doLogin(user, "api/auth/signin");
            Assert.assertTrue(Strings.isNotNullAndNotEmpty(token));
        }
    }

    @Given("The following INVALID credentials are")
    public void the_following_invalid_credentials_are(List<LoginCredentialRecord> credentials){

        for (LoginCredentialRecord credential : credentials) {
            try {
                var user = getUserByCredential(credential);
                RestAssurredUtil.doLogin(user, "api/auth/signin");
            }catch (Exception e){
                exceptions.add(e);
            }
        }
        assertEquals(exceptions.size(), credentials.size());
    }

    @Then("a UnrecognizedPropertyException should be thrown for all invalid credentials")
    public void thenAUsernameNotFoundExceptionShouldBeThrownForAllInvalidCredentials() {
        final int INVALID_CREDENTIALS_TABLE_SIZE = 3;
        assertEquals(exceptions.size(), INVALID_CREDENTIALS_TABLE_SIZE);
        for (Exception exception : exceptions) {
            assert exception instanceof RuntimeException;
            assertTrue(exception.getMessage().contains("UnrecognizedPropertyException"));
        }
    }


    @DataTableType
    public LoginCredentialRecord credentials(Map<String, String> row){
        return new LoginCredentialRecord(
                LoginDataTableValues.from(row.get("User")),
                LoginDataTableValues.from(row.get("Password"))
        );
    }

    private User getUserByCredential(LoginCredentialRecord credential){
        return new User(
                getUserByType(credential.typeUser()),
                getPasswordByType(credential.typePassword())
        );
    }

    private String getUserByType(LoginDataTableValues typeUser){
        return typeUser.get(user.username());
    }

    private String getPasswordByType(LoginDataTableValues typePassword){
        return typePassword.get(user.password());
    }
}
