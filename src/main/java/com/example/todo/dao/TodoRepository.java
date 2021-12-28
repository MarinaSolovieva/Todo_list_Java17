package com.example.todo.dao;

import com.example.todo.model.entity.Todo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    List<Todo> findByUserId(Long id);
}
