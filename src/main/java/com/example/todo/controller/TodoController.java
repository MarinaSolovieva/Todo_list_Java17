package com.example.todo.controller;

import com.example.todo.model.dto.TodoRequestDto;
import com.example.todo.model.dto.TodoResponseDto;
import com.example.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/todos")
@ResponseStatus(value = HttpStatus.OK)
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping()
    public List<TodoResponseDto> findTodoByUserId(@RequestParam Long userId) {
        return todoService.findByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteTodoById(@PathVariable Long id) {
        todoService.deleteById(id);
    }

    @PostMapping()
    public TodoResponseDto saveTodo(@Valid @RequestBody TodoRequestDto todoRequestDto) {
        return todoService.save(todoRequestDto);
    }

    @PutMapping("/{id}")
    public TodoResponseDto updateTodo(@Valid @RequestBody TodoRequestDto todoRequestDto,
                                      @PathVariable Long id) {
        return todoService.update(todoRequestDto, id);
    }
}
