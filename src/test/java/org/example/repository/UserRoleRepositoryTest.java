package org.example.repository;

import org.example.model.Role;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindRole() {
        Role role = new Role("ROLE_TEST");
        Role saved = roleRepository.save(role);
        assertNotNull(saved.getId());

        Optional<Role> found = roleRepository.findByName("ROLE_TEST");
        assertTrue(found.isPresent());
        assertEquals("ROLE_TEST", found.get().getName());
    }

    @Test
    void testSaveUserWithRoleAndFindByUsername() {
        Role roleUser = new Role("ROLE_USER_TEST");
        roleRepository.save(roleUser);

        User u = new User();
        u.setUsername("testuser");
        u.setPassword("secret");
        u.setEnabled(true);
        // assign role
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleUser);
        u.setRoles(roles);

        User saved = userRepository.save(u);
        assertNotNull(saved.getId());

        Optional<User> found = userRepository.findByUsername("testuser");
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
        assertNotNull(found.get().getRoles());
        assertFalse(found.get().getRoles().isEmpty());
        assertTrue(found.get().getRoles().stream().anyMatch(r -> "ROLE_USER_TEST".equals(r.getName())));
    }
}

