package com.prom.project.todolist.service;

import com.prom.project.todolist.dto.TokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtGenerator {

    TokenResponse generateJwt(UserDetails userDetails);

}
