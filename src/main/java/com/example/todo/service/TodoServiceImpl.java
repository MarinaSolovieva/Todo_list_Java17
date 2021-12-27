package com.example.todo.service;

import com.example.todo.dao.TodoRepository;
import com.example.todo.dao.UserRepository;
import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.dto.TodoRequestDto;
import com.example.todo.model.dto.TodoResponseDto;
import com.example.todo.model.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TodoMapper todoMapper;

    @Override
    public List<TodoResponseDto> findByUserId(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchUserIdException("There is no user with id = " + id);
        }
        return todoRepository.findByUserId(id)
                .stream()
                .map(todoMapper::entityToTodoResponseDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public TodoResponseDto save(TodoRequestDto todoRequestDto) {
        Todo todo = todoMapper.todoRequestDtoToEntity(todoRequestDto);
        todo.setUser(userRepository.findById(todoRequestDto.userId())
                .orElseThrow(() -> new NoSuchUserIdException("You cannot save todo for non-existent user with id = "
                        + todoRequestDto.userId())));
        return todoMapper.entityToTodoResponseDto(todoRepository.save(todo));
    }

    @Override
    public TodoResponseDto update(TodoRequestDto todoRequestDto, Long id) {
        Todo todo = todoMapper.todoRequestDtoToEntity(todoRequestDto);
        todo.setId(id);
        todo.setUser(userRepository.findById(todoRequestDto.userId())
                .orElseThrow(() -> new NoSuchUserIdException("You cannot update todo for non-existent user with id = "
                        + todoRequestDto.userId())));
        return todoMapper.entityToTodoResponseDto(todoRepository.save(todo));
    }
}
