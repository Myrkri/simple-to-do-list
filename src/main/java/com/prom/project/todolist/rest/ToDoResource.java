package com.prom.project.todolist.rest;

import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.service.ToDoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/list", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ToDoResource {

    private final ToDoService toDoService;

    @Operation(summary = "Api to request all ToDos")
    @GetMapping("/get-all")
    public ResponseEntity<List<ToDoDto>> getToDos() {
        return ResponseEntity.ok(toDoService.getToDos());
    }

    @Operation(summary = "Api to create a new ToDo")
    @PostMapping("/create")
    public ResponseEntity<ToDoDto> createTodo(@RequestBody ToDoDto toDoDTO) {
        return ResponseEntity.ok(toDoService.addToDo(toDoDTO));
    }

    @Operation(summary = "Api to update ToDo")
    @PutMapping("/update")
    public ResponseEntity<ToDoDto> updateTodo(@RequestBody ToDoDto toDoDTO) {
        return ResponseEntity.ok(toDoService.updateToDo(toDoDTO));
    }

    @Operation(summary = "Api to delete ToDo by ID")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTodo(@RequestParam Integer id) {
        toDoService.deleteToDo(id);
        return ResponseEntity.ok("Deletion Successful");
    }

}
