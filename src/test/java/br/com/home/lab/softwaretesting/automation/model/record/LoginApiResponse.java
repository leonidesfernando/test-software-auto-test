package br.com.home.lab.softwaretesting.automation.model.record;

import java.util.List;

public record LoginApiResponse(String token, String type, int id, String name, String username, String email, List<String> roles){}