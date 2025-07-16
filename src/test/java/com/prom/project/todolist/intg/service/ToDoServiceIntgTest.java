package com.prom.project.todolist.intg.service;

import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.entity.ToDoEntity;
import com.prom.project.todolist.repository.ToDoRepository;
import com.prom.project.todolist.service.ToDoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class ToDoServiceIntgTest {

    @Autowired
    private ToDoService toDoService;
    @Autowired
    private ToDoRepository toDoRepository;

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
}
