
package org.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.example.security.JwtUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @RequestParam(value = "token", required = false) String token,
            Authentication authentication) {

        if (token != null && !token.isBlank()) {
            try {
                String username = jwtUtil.extractUsername(token);
                List<String> roles = List.of();
                Map<String, Object> body = Map.of(
                        "username", username,
                        "roles", roles
                );
                return ResponseEntity.ok(body);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        String username = principal instanceof UserDetails
                ? ((UserDetails) principal).getUsername()
                : principal.toString();

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> body = Map.of(
                "username", username,
                "roles", roles
        );
        return ResponseEntity.ok(body);
    }
}