package com.prom.project.todolist.intg.service;

import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.entity.ToDoEntity;
import com.prom.project.todolist.repository.ToDoRepository;
import com.prom.project.todolist.service.ToDoService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class ToDoServiceIntgTest {

    @Autowired
    private ToDoService toDoService;
    @Autowired
    private ToDoRepository toDoRepository;

    @BeforeAll
    static void putJwtPrincipal() {
        var jwt = Jwt.withTokenValue("test")
                .header("Content-Type", "application/json")
                .subject("test_user")
                .claim("roles", List.of("ROLE_USER"))
                .build();

        var auth = new JwtAuthenticationToken(
                jwt,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("Create a new ToDo")
    void addNewToDo() {
        final ToDoDto toDoDTO = new ToDoDto();
        toDoDTO.setDescription("New Test Description");

        final ToDoDto result = toDoService.addToDo(toDoDTO);
        assertEquals(toDoDTO.getDescription(), result.getDescription());
        assertNotNull(result.getCreatedOn());
        assertNotNull(result.getId());
        assertTrue(toDoRepository.existsById(result.getId()));
    }

    @Test
    @DisplayName("Delete ToDo")
    void deleteToDo() {
        final ToDoEntity toDoEntity = new ToDoEntity();
        toDoEntity.setDescription("New Test Description");
        toDoEntity.setCreatedOn(LocalDate.now());
        toDoEntity.setDone(false);

        var entity = toDoRepository.save(toDoEntity);

        assertTrue(toDoRepository.existsById(entity.getId()));
        toDoService.deleteToDo(entity.getId());
        assertFalse(toDoRepository.existsById(entity.getId()));
    }

    @AfterEach
    void tearDown() {
        toDoRepository.deleteAll();
    }

    @AfterAll
    static void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
