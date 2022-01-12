package com.example.todo.controller;

import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.model.dto.TodoRequestDto;
import com.example.todo.model.dto.TodoResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql("/fill-tables-before.sql")
@Sql(value = {"/clean-tables-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TodoControllerIT {

    private static final String NON_EXISTENT_USER_ID = "4";
    private static final long NON_EXISTENT_TODO_ID = 5L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetTodosForExistingUserThenReturns200AndTodoResponse() throws Exception {
        String actual = mockMvc.perform(get("/todos")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TodoResponseDto todoFirst = new TodoResponseDto(1L, "Feed the cat", 1L);
        TodoResponseDto todoSecond = new TodoResponseDto(2L, "Go to the grocery store", 1L);

        assertNotNull(actual);
        assertEquals(objectMapper.writeValueAsString(Arrays.asList(todoFirst, todoSecond)), actual);
    }

    @Test
    void whenGetTodosForNonExistentUserThenReturns404AndExceptionThrown() throws Exception {
        mockMvc.perform(get("/todos")
                .param("userId", NON_EXISTENT_USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertEquals(requireNonNull(result.getResolvedException()).getClass(), NoSuchUserIdException.class);
                });
    }

    @Test
    void whenDeleteExistingTodoThenReturns200() throws Exception {
        mockMvc.perform(delete("/todos/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteNonExistentTodoThenReturns404AndExceptionThrown() throws Exception {
        mockMvc.perform(delete("/todos/{id}", NON_EXISTENT_TODO_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertEquals(requireNonNull(result.getResolvedException()).getClass(), EmptyResultDataAccessException.class);
                });
    }

    @Test
    void whenSaveValidTodoThenReturns201AndTodoResponse() throws Exception {
        TodoRequestDto todoRequestDto = new TodoRequestDto("Clean the room", 1L);
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.description").value("Clean the room"))
                .andExpect(jsonPath("$.userId").value("1"));

    }

    @Test
    void whenSaveNotValidTodoThenReturns401AndExceptionThrown() throws Exception {
        TodoRequestDto todoRequestDto = new TodoRequestDto("Clean the room", null);
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertEquals(requireNonNull(result.getResolvedException()).getClass(), MethodArgumentNotValidException.class);
                });
    }

    @Test
    void whenUpdateValidTodoThenReturns201AndTodoResponse() throws Exception {
        TodoRequestDto todoRequestDto = new TodoRequestDto("Clean the room", 1L);
        String actual = mockMvc.perform(put("/todos/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(actual);
        assertEquals(objectMapper.writeValueAsString(new TodoResponseDto(2L, "Clean the room", 1L)), actual);
    }

    @Test
    void whenUpdateNotValidTodoThenReturns401AndExceptionThrown() throws Exception {
        TodoRequestDto todoRequestDto = new TodoRequestDto("Clean the room", null);
        mockMvc.perform(put("/todos/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertEquals(requireNonNull(result.getResolvedException()).getClass(), MethodArgumentNotValidException.class);
                });
    }
}
