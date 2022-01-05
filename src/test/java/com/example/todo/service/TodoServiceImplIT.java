package com.example.todo.service;

import com.example.todo.dao.TodoRepository;
import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.mapper.TodoMapperImpl;
import com.example.todo.model.dto.TodoRequestDto;
import com.example.todo.model.dto.TodoResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;

import static com.example.todo.Utils.createTodo;
import static com.example.todo.Utils.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplIT {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserService userService;
    @Spy
    private final TodoMapper todoMapper = new TodoMapperImpl();
    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void testFindByUserId() {
        var user = createUser("Marina", "Solovieva", 1L);
        var firstTodo = createTodo("Feed the cat", 1L, user);
        var secondTodo = createTodo("Bake the pie", 2L, user);
        when(userService.existsByUserId(1L)).thenReturn(true);
        when(todoRepository.findByUserId(1L)).thenReturn(Arrays.asList(firstTodo, secondTodo));

        List<TodoResponseDto> actual = todoService.findByUserId(1L);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        verify(todoRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testFindByUserIdWithNoSuchUserIdException() {
        when(userService.existsByUserId(1L)).thenReturn(false);
        assertThrows(NoSuchUserIdException.class, () -> todoService.findByUserId(1L));
    }

    @Test
    void testDeleteById() {
        todoService.deleteById(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdWithEmptyResultDataAccessException() {
        doThrow(EmptyResultDataAccessException.class).when(todoRepository).deleteById(1L);
        assertThrows(EmptyResultDataAccessException.class, () -> todoService.deleteById(1L));
    }

    @Test
    void testSave() {
        setDataForSaveAndUpdate();
        TodoResponseDto actual = todoService.save(new TodoRequestDto("Feed a cat", 1L));
        assertNotNull(actual);
        assertEquals(new TodoResponseDto(1L, "Feed a cat", 1L), actual);
    }

    @Test
    void testSaveWithNoSuchUserIdException() {
        TodoRequestDto todoRequestDto = new TodoRequestDto("Feed a cat", 1L);
        doThrow(NoSuchUserIdException.class).when(userService).findUserById(1L);
        assertThrows(NoSuchUserIdException.class, () -> todoService.save(todoRequestDto));
    }

    @Test
    void testUpdate() {
        setDataForSaveAndUpdate();
        TodoResponseDto actual = todoService.update(new TodoRequestDto("Feed a cat", 1L), 1L);
        assertNotNull(actual);
        assertEquals(new TodoResponseDto(1L, "Feed a cat", 1L), actual);
    }

    @Test
    void testUpdateWithNoSuchUserIdException() {
        TodoRequestDto todoRequestDto = new TodoRequestDto("Feed a cat", 1L);
        doThrow(NoSuchUserIdException.class).when(userService).findUserById(1L);
        assertThrows(NoSuchUserIdException.class, () -> todoService.update(todoRequestDto, 1L));
    }

    private void setDataForSaveAndUpdate() {
        var user = createUser("Marina", "Solovieva", 1L);
        var todo = createTodo("Feed a cat", 1L, user);
        when(userService.findUserById(1L)).thenReturn(user);
        when(todoRepository.save(any())).thenReturn(todo);
    }
}
