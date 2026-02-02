package com.raghav.saas.core.security;

import java.util.List;

public record AuthPrincipal(
        String userId,
        String tenantId,
        String email,
        List<String> roles
) {}
