package com.example.todo.controller;

import com.example.todo.model.dto.TodoRequestDto;
import com.example.todo.model.dto.TodoResponseDto;
import com.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("")
    public List<TodoResponseDto> findTodoByUserId(@RequestParam Long userId) {
        return todoService.findByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteTodoById(@PathVariable Long id) {
        todoService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<TodoResponseDto> saveTodo(@Valid @RequestBody TodoRequestDto todoRequestDto) {
       return new ResponseEntity<>(todoService.save(todoRequestDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(@Valid @RequestBody TodoRequestDto todoRequestDto,
                                                      @PathVariable Long id) {
        return new ResponseEntity<>(todoService.update(todoRequestDto, id), HttpStatus.OK);
    }


}
