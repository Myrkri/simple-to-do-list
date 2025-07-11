package com.prom.project.todolist.service;

import com.prom.project.todolist.dto.UserDto;

public interface UserService {

    String login(UserDto user);
    String register(UserDto user);
    UserDto getCurrentUser();
}
