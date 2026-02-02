package com.raghav.saas.core.api;

import com.raghav.saas.core.security.AuthPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}")
public class TenantAdminController {

    @PreAuthorize("hasRole('TENANT_ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@PathVariable String tenantId,
                                        @Valid @RequestBody CreateUserRequest req,
                                        Authentication auth) {

        var p = (AuthPrincipal) auth.getPrincipal();

        // Tenant isolation check (path must match token)
        if (!tenantId.equals(p.tenantId())) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "FORBIDDEN",
                    "message", "tenantId mismatch"
            ));
        }

        // For Day 3: return a fake userId (DB comes later)
        String newUserId = "u_" + UUID.randomUUID();

        return ResponseEntity.ok(Map.of(
                "userId", newUserId,
                "tenantId", tenantId,
                "email", req.email(),
                "role", req.role(),
                "createdBy", p.userId()
        ));
    }
}
