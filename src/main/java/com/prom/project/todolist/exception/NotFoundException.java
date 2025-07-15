package com.prom.project.todolist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(Integer id) {
        super(String.format("ToDo with id `%s` not found", id));
        log.error("ToDo with id `{}` not found", id);
    }
}
