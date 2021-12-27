package com.example.todo.mapper;

import com.example.todo.model.dto.TodoRequestDto;
import com.example.todo.model.dto.TodoResponseDto;
import com.example.todo.model.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Todo todoRequestDtoToEntity(TodoRequestDto todoRequestDto);

    @Mapping(source = "todo.user.id", target = "userId")
    TodoResponseDto entityToTodoResponseDto(Todo todo);
}
