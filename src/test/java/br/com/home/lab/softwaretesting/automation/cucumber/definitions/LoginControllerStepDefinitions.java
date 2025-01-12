package br.com.home.lab.softwaretesting.automation.cucumber.definitions;

import br.com.home.lab.softwaretesting.automation.model.User;
import br.com.home.lab.softwaretesting.automation.model.record.LoginCredentialRecord;
import br.com.home.lab.softwaretesting.automation.restassured.RestAssurredUtil;
import br.com.home.lab.softwaretesting.automation.util.DataGen;
import br.com.home.lab.softwaretesting.automation.util.LoadConfigurationUtil;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import org.testng.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.home.lab.softwaretesting.automation.cucumber.definitions.LoginDataTableValues.VALID_PASSWORD;
import static br.com.home.lab.softwaretesting.automation.cucumber.definitions.LoginDataTableValues.VALID_USER;
import static org.testng.Assert.assertEquals;

public class LoginControllerStepDefinitions {

    private static final User user = LoadConfigurationUtil.getUser();
    private List<Exception> exceptions = new ArrayList<>();

    @Given("The following credentials then")
    public void the_following_credentials_then(List<LoginCredentialRecord> credentials){

        for (LoginCredentialRecord credential : credentials) {
            User user = getUserByCredential(credential);
            String token =  RestAssurredUtil.doLogin(user, "api/auth/signin");
            Assert.assertTrue(Strings.isNotNullAndNotEmpty(token));

            System.out.println("Credential: " + credential);
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
        for (Exception exception : exceptions) {
            assert exception instanceof UnrecognizedPropertyException;
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
        if(typeUser == VALID_USER)
            return user.username();
        return user.username() + DataGen.number(5);
    }

    private String getPasswordByType(LoginDataTableValues typePassword){
        if(typePassword == VALID_PASSWORD){
            return user.password();
        }
        return DataGen.productName();
    }
}
