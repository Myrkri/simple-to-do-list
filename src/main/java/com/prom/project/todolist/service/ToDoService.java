package com.prom.project.todolist.service;

import com.prom.project.todolist.dto.ToDoDto;

import java.util.List;

public interface ToDoService {
    List<ToDoDto> getToDos();
    ToDoDto addToDo(ToDoDto toDoListDTO);
    ToDoDto updateToDo(ToDoDto toDoListDTO);
    void deleteToDo(Integer id);
}
