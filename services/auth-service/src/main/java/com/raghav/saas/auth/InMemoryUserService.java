package com.raghav.saas.auth;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InMemoryUserService {

    private final Map<String, String> passwordByEmail = Map.of(
            "admin@t1.com", "Password123!",
            "user@t1.com", "Password123!"
    );

    private final Map<String, AppUser> userByEmail = Map.of(
            "admin@t1.com", new AppUser("u1", "t1", List.of("TENANT_ADMIN"), "admin@t1.com"),
            "user@t1.com", new AppUser("u2", "t1", List.of("TENANT_USER"), "user@t1.com")
    );

    public AppUser authenticate(String email, String password) {
        var expected = passwordByEmail.get(email);
        if (expected == null || !expected.equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return userByEmail.get(email);
    }
}
