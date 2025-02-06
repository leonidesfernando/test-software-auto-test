package br.com.home.lab.softwaretesting.automation.model.record;

import java.util.List;

public record UserProfile(
        int id,
        String username,
        String email,
        List<Authority> authorities,
        boolean enabled,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean accountNonLocked
) {
    public record Authority(String authority) {}
}