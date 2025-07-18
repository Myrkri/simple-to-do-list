package com.prom.project.todolist.repository;

import com.prom.project.todolist.entity.ToDoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoEntity, Integer> {

    List<ToDoEntity> findByUsername(String username);
}
