package com.prom.project.todolist.intg.rest;

import com.prom.project.todolist.entity.UserEntity;
import com.prom.project.todolist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceIntgTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerNewUser() throws Exception {
        mvc.perform(post("/api/signup")
                        .contentType("application/json")
                        .content("""
                     {"username":"test","password":"password"}
                     """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").isNotEmpty());
    }

    @Test
    void login() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("test1");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole("ROLE_USER");
        var result = repository.save(user);

        mvc.perform(post("/api/login")
                .contentType("application/json")
                .content("""
                     {"username":"test1","password":"password"}
                     """)).andExpect(status().isOk())
                .andExpect(jsonPath("token").isNotEmpty());

        assertTrue(repository.existsByUsername(result.getUsername()));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}
