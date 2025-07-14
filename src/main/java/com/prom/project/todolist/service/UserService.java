package com.prom.project.todolist.service;

import com.prom.project.todolist.dto.TokenResponse;
import com.prom.project.todolist.dto.UserDto;

public interface UserService {

    TokenResponse login(UserDto user);
    TokenResponse register(UserDto user);
    UserDto getCurrentUser();
}
