package com.example.todo.service;

import com.example.todo.model.dto.TodoRequestDto;
import com.example.todo.model.dto.TodoResponseDto;

import java.util.List;

public interface TodoService {

    List<TodoResponseDto> findByUserId(Long id);

    void deleteById(Long id);

    TodoResponseDto save(TodoRequestDto todoRequestDto);

    TodoResponseDto update(TodoRequestDto todoRequestDto, Long id);
}
