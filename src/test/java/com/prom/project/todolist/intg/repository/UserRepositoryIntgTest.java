package com.prom.project.todolist.intg.repository;

import com.prom.project.todolist.entity.UserEntity;
import com.prom.project.todolist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntgTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByUsername_returnsTrue_whenUserPresent() {
        final UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("test");

        assertTrue(exists);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}

