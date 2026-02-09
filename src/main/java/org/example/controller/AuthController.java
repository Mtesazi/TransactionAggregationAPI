package org.example.controller;

import org.example.dto.LoginResponseDto;
import org.example.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${auth.debug:false}")
    private boolean authDebug;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            log.debug("Login attempt with missing username");
            return ResponseEntity.badRequest().build();
        }

        String username = request.getUsername().trim();
        log.debug("Login attempt for user={}", username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            );

            Object principal = authentication.getPrincipal();
            String resolvedUsername = principal instanceof UserDetails
                    ? ((UserDetails) principal).getUsername()
                    : principal.toString();

            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            UserDetails userDetails = principal instanceof UserDetails
                    ? (UserDetails) principal
                    : null;

            String token;
            if (userDetails != null) {
                token = jwtUtil.generateToken(userDetails);
            } else {
                // fallback: create a simple UserDetails with resolved roles so token contains authorities
                List<GrantedAuthority> granted = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(resolvedUsername, "", granted));
            }

            // Don't log full token in production; log a masked/truncated preview for debugging
            String tokenPreview = token == null ? "" : (token.length() > 32 ? token.substring(0, 32) + "..." : token);
            log.info("User '{}' authenticated successfully; roles={}, tokenPreview={}", resolvedUsername, roles, tokenPreview);

            LoginResponseDto resp = new LoginResponseDto(token, "Bearer", resolvedUsername, roles);
            return ResponseEntity.ok(resp);
        } catch (org.springframework.security.core.AuthenticationException ex) {
            log.info("Authentication failed for user {}: {}", username, ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Unexpected error during login for user {}: {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/debug-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> debugToken(@RequestParam(value = "username", required = false) String username) {
        if (!authDebug) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "debug token endpoint disabled"));
        }
        if (username == null || username.isBlank()) username = "user";
        // create a UserDetails for the given username with USER
        UserDetails u = new org.springframework.security.core.userdetails.User(username, "", List.of(new SimpleGrantedAuthority("USER")));
        String token = jwtUtil.generateToken(u);
        List<String> roles = List.of("USER");
        LoginResponseDto resp = new LoginResponseDto(token, "Bearer", username, roles);
        String tokenPreview = token == null ? "" : (token.length() > 32 ? token.substring(0, 32) + "..." : token);
        log.info("Generated debug token for user={}; tokenPreview={}", username, tokenPreview);
        return ResponseEntity.ok(resp);
    }
}
