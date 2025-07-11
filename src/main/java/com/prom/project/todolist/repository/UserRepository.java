package com.prom.project.todolist.repository;

import com.prom.project.todolist.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    boolean existsByUsername(String username);
}
