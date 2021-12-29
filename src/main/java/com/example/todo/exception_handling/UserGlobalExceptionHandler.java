package com.example.todo.exception_handling;

import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public UserIncorrectData handleException(NoSuchUserIdException exception) {
        return new UserIncorrectData((exception.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public UserIncorrectData handleException(EmptyResultDataAccessException exception) {
        return new UserIncorrectData((exception.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public UserIncorrectData handleException(Exception exception) {
        return new UserIncorrectData((exception.getMessage()));
    }
}
