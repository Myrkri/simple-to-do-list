package com.prom.project.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private Date issued;
    private Date expires;

    public String getType() {
        return "Bearer";
    }
}
