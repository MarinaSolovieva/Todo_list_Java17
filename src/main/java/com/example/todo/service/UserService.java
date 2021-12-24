package com.example.todo.service;

import com.example.todo.model.dto.UserRequestDto;
import com.example.todo.model.dto.UserResponseDto;

public interface UserService {

    UserResponseDto findById(Long id);

    void deleteById(Long id);

    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto update(UserRequestDto userRequestDto, Long id);
}
