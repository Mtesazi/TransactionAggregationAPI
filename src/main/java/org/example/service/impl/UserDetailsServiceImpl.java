// java
package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import org.example.repository.UserRepository;
import java.util.stream.Collectors;

// Implement Spring Security's UserDetailsService so it wires where expected
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to load from repository first
        return userRepository.findByUsername(username)
                .map(domain -> {
                    List<SimpleGrantedAuthority> authorities = domain.getRoles().stream()
                            .map(r -> new SimpleGrantedAuthority(r.getName()))
                            .collect(Collectors.toList());
                    return new User(domain.getUsername(), domain.getPassword(), authorities);
                })
                .orElseGet(() -> {
                    // Fallback: simple test user for local runs: username "user", password "password"
                    if ("user".equals(username)) {
                        String encodedPassword = passwordEncoder.encode("password");
                        return new User(
                                "user",
                                encodedPassword,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                    }
                    throw new UsernameNotFoundException("User not found: " + username);
                });
    }
}