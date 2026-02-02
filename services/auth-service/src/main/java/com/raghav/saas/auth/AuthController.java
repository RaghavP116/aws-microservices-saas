package com.raghav.saas.auth;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final InMemoryUserService userService;
    private final JwtService jwtService;

    public AuthController(InMemoryUserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        var user = userService.authenticate(req.email(), req.password());
        var token = jwtService.createToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
