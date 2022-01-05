package com.example.todo;

import com.example.todo.model.entity.Todo;
import com.example.todo.model.entity.User;

public final class Utils {

    private Utils() {}

    public static User createUser(String name, String surname, Long id) {
        User user = new User();
        user.setSurname(surname);
        user.setName(name);
        user.setId(id);
        return user;
    }

    public static Todo createTodo(String description, Long id, User user) {
        Todo todo = new Todo();
        todo.setDescription(description);
        todo.setId(id);
        todo.setUser(user);
        return todo;
    }
}
