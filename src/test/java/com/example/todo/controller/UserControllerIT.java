package com.example.todo.controller;

import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.model.dto.UserRequestDto;
import com.example.todo.model.dto.UserResponseDto;
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
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("/fill-users-table-before.sql")
@Sql(value = {"/clean-tables-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerIT {

    private static final long NON_EXISTENT_USER_ID = 4L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetExistingUserThenReturns200AndUserResponse() throws Exception {
        String actual = mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(actual);
        assertEquals(objectMapper.writeValueAsString(new UserResponseDto(1L, "Marina", "Solovieva")), actual);
    }

    @Test
    void whenGetNonExistentUserThenReturns404AndExceptionThrown() throws Exception {
        mockMvc.perform(get("/users/{id}", NON_EXISTENT_USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertEquals(requireNonNull(result.getResolvedException()).getClass(), NoSuchUserIdException.class);
                });
    }

    @Test
    void whenDeleteExistingUserThenReturns200() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteNonExistentUserThenReturns404AndExceptionThrown() throws Exception {
        mockMvc.perform(delete("/users/{id}", NON_EXISTENT_USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assertEquals(requireNonNull(result.getResolvedException()).getClass(), EmptyResultDataAccessException.class);
                });
    }

    @Test
    void whenSaveUserThenReturns201AndUserResponse() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("Ulya", "Kotul");
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ulya"))
                .andExpect(jsonPath("$.surname").value("Kotul"));
    }

    @Test
    void whenUpdateUserThenReturns200AndUserResponse() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("Marina", "Barkhatova");
        String actual = mockMvc.perform(put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(actual);
        assertEquals(objectMapper.writeValueAsString(new UserResponseDto(1L, "Marina", "Barkhatova")), actual);
    }
}
