package com.prom.project.todolist.intg.rest;

import com.prom.project.todolist.repository.ToDoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToDoResourceIntgTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ToDoRepository repository;

    @Test
    void createNewToDo() throws Exception {
        mockMvc.perform(post("/api/list/create")
                        .with(jwt().jwt((jwt) -> jwt.subject("test_user")
                                .claim("roles", "ROLE_USER")))
                .contentType("application/json")
                .content(""" 
                  {"description":"TEST"}
                  """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("TEST"))
                .andExpect(jsonPath("id").value(1));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}
