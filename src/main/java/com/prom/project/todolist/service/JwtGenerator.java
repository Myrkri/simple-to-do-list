package com.prom.project.todolist.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtGenerator {

    String generateJwt(UserDetails userDetails);

}
