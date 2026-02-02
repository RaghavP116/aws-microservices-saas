package com.raghav.saas.core.api;

import com.raghav.saas.core.security.AuthPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        var p = (AuthPrincipal) auth.getPrincipal();
        return Map.of(
                "userId", p.userId(),
                "tenantId", p.tenantId(),
                "email", p.email(),
                "roles", p.roles()
        );
    }
}
