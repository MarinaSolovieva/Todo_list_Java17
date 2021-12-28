package com.example.todo.service;

import com.example.todo.model.dto.UserRequestDto;
import com.example.todo.model.dto.UserResponseDto;
import com.example.todo.model.entity.User;

public interface UserService {

    UserResponseDto getById(Long id);

    User findUserById(Long id);

    void deleteById(Long id);

    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto update(UserRequestDto userRequestDto, Long id);

    boolean existsByUserId(Long id);
}
