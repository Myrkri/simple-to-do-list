package com.prom.project.todolist.rest;

import com.prom.project.todolist.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @Operation(summary = "Api to login into the system")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        //TODO return token
        return null;
    }

    @Operation(summary = "Api to register user into the system")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        //TODO return token
        return null;
    }

    @Operation(summary = "Api to get currently logged in user data")
    @GetMapping("/me")
    public ResponseEntity<User> currentUser() {
        return null;
    }
}
