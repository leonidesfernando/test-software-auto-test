package br.com.home.lab.softwaretesting.automation.model.record;


public record UserProfile(
        long id,
        String username,
        String name,
        String email,
        String password,
        String[] roles
){}
