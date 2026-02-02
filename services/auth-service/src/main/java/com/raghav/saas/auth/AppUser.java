package com.raghav.saas.auth;

import java.util.List;

public record AppUser(
        String userId,
        String tenantId,
        List<String> roles,
        String email
) {}
