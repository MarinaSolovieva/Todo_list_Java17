package com.example.todo.model.dto;

import javax.validation.constraints.NotNull;

public record TodoRequestDto(String description, @NotNull Long userId) {

}
