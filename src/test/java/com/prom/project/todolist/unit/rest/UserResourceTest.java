package com.prom.project.todolist.unit.rest;

import com.prom.project.todolist.config.SecurityConfig;
import com.prom.project.todolist.dto.TokenResponse;
import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.rest.UserResource;
import com.prom.project.todolist.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserResource.class)
@Import(SecurityConfig.class)
public class UserResourceTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void loginEndpointReturnsAccessToken() throws Exception {
        when(userService.login(any(UserDto.class))).thenReturn(new TokenResponse("jwt", new Date(), new Date()));

        mvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content("""
                     {"username":"test","password":"test"}
                     """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt"));
    }
}
