package com.prom.project.todolist.unit.rest;

import com.prom.project.todolist.config.SecurityConfig;
import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.rest.ToDoResource;
import com.prom.project.todolist.service.ToDoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ToDoResource.class)
@Import(SecurityConfig.class)
public class ToDoResourceTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ToDoService toDoService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void getToDos_whenAuthenticated_shouldReturnToDoList() throws Exception {
        final ToDoDto responseDto = new ToDoDto();
        responseDto.setId(1);
        responseDto.setDescription("TEST");
        responseDto.setCreatedOn(LocalDate.now());

        when(toDoService.getToDos()).thenReturn(List.of(responseDto));

        mvc.perform(get("/api/list/get-all")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].description").value("TEST"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getToDos_whenNotAuthenticated_shouldReturnUnauthorized() throws Exception {
        mvc.perform(get("/api/list/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
