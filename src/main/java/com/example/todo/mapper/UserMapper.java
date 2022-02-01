package com.example.todo.mapper;

import com.example.todo.model.dto.UserRequestDto;
import com.example.todo.model.dto.UserResponseDto;
import com.example.todo.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "todos", ignore = true)
    User userRequestDtoToEntity(UserRequestDto userRequestDto);

    UserResponseDto entityToUserResponseDto(User user);
}
