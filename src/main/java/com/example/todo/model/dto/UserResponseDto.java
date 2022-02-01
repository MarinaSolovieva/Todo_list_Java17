package com.example.todo.model.dto;

import lombok.Data;

@Data
public class UserResponseDto {

    private final Long id;
    private final String name;
    private final String surname;
}
