package com.prom.project.todolist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ToDoDto {
    private Integer id;
    private String description;
    private LocalDate due;
    private LocalDate createdOn;
    private boolean isDone;
    private LocalDate completedOn;
    @JsonIgnore
    private String username;
}
