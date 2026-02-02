package com.raghav.saas.core.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring("Bearer ".length()).trim();

        try {
            Claims claims = jwtService.parseAndValidate(token);

            String userId = claims.getSubject();
            String tenantId = claims.get("tenantId", String.class);
            String email = claims.get("email", String.class);

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles", List.class);

            var authorities = roles.stream()
                    // Spring expects "ROLE_" prefix for hasRole checks
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toList());

            // Principal will store key tenant info too
            var principal = new AuthPrincipal(userId, tenantId, email, roles);

            var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            // Invalid token -> clear auth and continue (will get 401 on protected endpoints)
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
