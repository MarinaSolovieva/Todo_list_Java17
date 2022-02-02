package com.example.todo.service;

import com.example.todo.dao.UserRepository;
import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.mapper.UserMapper;
import com.example.todo.model.dto.UserRequestDto;
import com.example.todo.model.dto.UserResponseDto;
import com.example.todo.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "userCache")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Cacheable(key = "#id")
    public UserResponseDto getById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserIdException("There is no user with id = " + id));
        return userMapper.entityToUserResponseDto(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserIdException("There is no user with id = " + id));
    }

    @Override
    public boolean existsByUserId(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User user = userRepository.save(userMapper.userRequestDtoToEntity(userRequestDto));
        return userMapper.entityToUserResponseDto(user);
    }

    @Override
    @CachePut(key = "#id")
    public UserResponseDto update(UserRequestDto userRequestDto, Long id) {
        User user = userMapper.userRequestDtoToEntity(userRequestDto);
        user.setId(id);
        return userMapper.entityToUserResponseDto(userRepository.save(user));
    }
}
