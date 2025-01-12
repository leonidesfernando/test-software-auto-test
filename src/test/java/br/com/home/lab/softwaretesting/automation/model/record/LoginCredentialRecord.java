package br.com.home.lab.softwaretesting.automation.model.record;

import br.com.home.lab.softwaretesting.automation.cucumber.definitions.LoginDataTableValues;

public record LoginCredentialRecord(LoginDataTableValues typeUser, LoginDataTableValues typePassword) {
}
