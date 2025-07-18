package com.prom.project.todolist.unit.service;

import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.entity.ToDoEntity;
import com.prom.project.todolist.mapper.ToDoMapper;
import com.prom.project.todolist.repository.ToDoRepository;
import com.prom.project.todolist.service.UserService;
import com.prom.project.todolist.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToDoServiceTest {

    @Mock
    private ToDoRepository toDoRepository;
    @Mock
    private ToDoMapper toDoMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private ToDoServiceImpl toDoService;

    private ToDoDto inputDto;

    @BeforeEach
    void setUp() {
        inputDto = new ToDoDto();
        inputDto.setDescription("TEST");
    }

    @Test
    @DisplayName("Successful addition to DB")
    void addToDo_whenValidDtoProvided_shouldSaveAndReturnDto() {
        final ToDoDto expected = new ToDoDto();
        expected.setId(1);
        expected.setDescription("TEST");
        expected.setCreatedOn(LocalDate.now());

        final ToDoEntity mappedEntity = new ToDoEntity();
        mappedEntity.setDescription("TEST");

        final ToDoEntity savedEntity = new ToDoEntity();
        savedEntity.setId(1);
        savedEntity.setDescription("TEST");
        savedEntity.setCreatedOn(LocalDate.now());

        when(userService.getCurrentUser()).thenReturn(new UserDto().setUsername("test"));
        when(toDoMapper.toEntity(any(ToDoDto.class))).thenReturn(mappedEntity);
        when(toDoRepository.save(any(ToDoEntity.class))).thenReturn(savedEntity);
        when(toDoMapper.toDto(any(ToDoEntity.class))).thenReturn(expected);

        final ToDoDto result = toDoService.addToDo(inputDto);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getDescription(), result.getDescription());
        assertNotNull(result.getCreatedOn());
    }

    @Test
    @DisplayName("Exception if DTO is NULL")
    void addToDo_whenDtoIsNull_shouldThrowNullPointerException() {
        final NullPointerException exception = assertThrows(NullPointerException.class, () -> toDoService.addToDo(null));

        assertEquals("There is no data to add", exception.getMessage());
    }

    @Test
    @DisplayName("Exception if Description is NULL")
    void addToDo_whenDescriptionIsEmpty_shouldThrowNullPointerException() {
        inputDto.setDescription("");

        final NullPointerException exception = assertThrows(NullPointerException.class, () -> toDoService.addToDo(inputDto));

        assertEquals("There is no description provided", exception.getMessage());
    }
}
