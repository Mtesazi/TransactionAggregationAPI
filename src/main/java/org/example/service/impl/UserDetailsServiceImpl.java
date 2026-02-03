// java
package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Simple test user for local runs: username "user", password "password"
        if ("user".equals(username)) {
            String encodedPassword = passwordEncoder.encode("password");
            return new User(
                    "user",
                    encodedPassword,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}