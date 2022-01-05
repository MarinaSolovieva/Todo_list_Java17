package com.example.todo.service;

import com.example.todo.dao.UserRepository;
import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.mapper.UserMapper;
import com.example.todo.mapper.UserMapperImpl;
import com.example.todo.model.dto.UserRequestDto;
import com.example.todo.model.dto.UserResponseDto;
import com.example.todo.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

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
class UserServiceImplIT {

    @Mock
    private UserRepository userRepository;
    @Spy
    private final UserMapper userMapper = new UserMapperImpl();
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetById() {
        var user = createUser("Marina", "Solovieva", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDto actual = userService.getById(1L);

        assertNotNull(actual);
        assertEquals(new UserResponseDto(1L, "Marina", "Solovieva"), actual);
    }

    @Test
    void testGetByIdWithNoSuchUserIdException() {
        doThrow(NoSuchUserIdException.class).when(userRepository).findById(1L);
        assertThrows(NoSuchUserIdException.class, () -> userService.getById(1L));
    }

    @Test
    void testFindUserById() {
        var user = createUser("Marina", "Solovieva", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User actual = userService.findUserById(1L);

        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void testFindUserByIdWithNoSuchUserIdException() {
        doThrow(NoSuchUserIdException.class).when(userRepository).findById(1L);
        assertThrows(NoSuchUserIdException.class, () -> userService.findUserById(1L));
    }

    @Test
    void testDeleteById() {
        userService.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdWithEmptyResultDataAccessException() {
        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(1L);
        assertThrows(EmptyResultDataAccessException.class, () -> userService.deleteById(1L));
    }

    @Test
    void testSave() {
        setDataForSaveAndUpdate();
        UserResponseDto actual = userService.save(new UserRequestDto("Marina", "Solovieva"));
        assertNotNull(actual);
        assertEquals(new UserResponseDto(1L, "Marina", "Solovieva"), actual);

    }

    @Test
    void testUpdate() {
        setDataForSaveAndUpdate();
        UserResponseDto actual = userService.update(new UserRequestDto("Marina", "Solovieva"), 1L);
        assertNotNull(actual);
        assertEquals(new UserResponseDto(1L, "Marina", "Solovieva"), actual);
    }

    private void setDataForSaveAndUpdate() {
        var user = createUser("Marina", "Solovieva", 1L);
        when(userRepository.save(any())).thenReturn(user);
    }
}
