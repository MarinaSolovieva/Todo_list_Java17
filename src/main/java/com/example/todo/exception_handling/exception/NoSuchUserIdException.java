package com.example.todo.exception_handling.exception;

public class NoSuchUserIdException extends RuntimeException {
    public NoSuchUserIdException(String message) {
        super(message);
    }
}
