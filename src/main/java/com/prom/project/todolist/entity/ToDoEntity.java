package com.prom.project.todolist.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "list")
public class ToDoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "due")
    private LocalDate due;

    @Column(name = "created_on", nullable = false)
    private LocalDate createdOn;

    @Column(name = "is_done", nullable = false)
    private boolean isDone;

    @Column(name = "completed_on")
    private LocalDate completedOn;

    @Column(name = "username")
    private String username;

}
