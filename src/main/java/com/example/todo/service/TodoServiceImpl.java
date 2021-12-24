package com.example.todo.service;

import com.example.todo.dao.TodoRepository;
import com.example.todo.dao.UserRepository;
import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.model.dto.TodoRequestDto;
import com.example.todo.model.dto.TodoResponseDto;
import com.example.todo.model.entity.Todo;
import com.example.todo.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TodoResponseDto> findByUserId(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchUserIdException("There is no user with id = " + id);
        }
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        List<Todo> todos = todoRepository.findByUserId(id);
        if (isNotEmpty(todos)) {
            todos.forEach(todo -> todoResponseDtos.add(convertTodoToTodoResponseDto(todo)));
        }
        return todoResponseDtos;
    }

    @Override
    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public TodoResponseDto save(TodoRequestDto todoRequestDto) {
        Optional<User> user = userRepository.findById(todoRequestDto.userId());
        if (user.isEmpty()) {
            throw new NoSuchUserIdException("You cannot save todo for non-existent user with id = "
                    + todoRequestDto.userId());
        }
        Todo todo = convertTodoRequestDtoToTodo(todoRequestDto);
        todo.setUser(user.get());
        return convertTodoToTodoResponseDto(todoRepository.save(todo));
    }

    @Override
    public TodoResponseDto update(TodoRequestDto todoRequestDto, Long id) {
        Optional<User> user = userRepository.findById(todoRequestDto.userId());
        if (user.isEmpty()) {
            throw new NoSuchUserIdException("You cannot update todo for non-existent user with id = "
                    + todoRequestDto.userId());
        }
        Todo todo = convertTodoRequestDtoToTodo(todoRequestDto);
        todo.setId(id);
        todo.setUser(user.get());
        return convertTodoToTodoResponseDto(todoRepository.save(todo));

    }

    private Todo convertTodoRequestDtoToTodo(TodoRequestDto todoRequestDto) {
        return new Todo(todoRequestDto.description());
    }

    private TodoResponseDto convertTodoToTodoResponseDto(Todo todo) {
        return new TodoResponseDto(todo.getId(), todo.getDescription(), todo.getUser().getId());
    }
}
