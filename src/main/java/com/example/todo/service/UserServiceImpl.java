package com.example.todo.service;

import com.example.todo.dao.UserRepository;
import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.mapper.UserMapper;
import com.example.todo.model.dto.UserRequestDto;
import com.example.todo.model.dto.UserResponseDto;
import com.example.todo.model.entity.User;
import com.example.todo.model.redis.UserRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.TRUE;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER = "USER";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RedisTemplate<String, UserRedis> redisTemplate;

    @Override
    public UserResponseDto getById(Long id) {
        if (TRUE.equals(redisTemplate.opsForHash().hasKey(USER, id))) {
            return userMapper.redisEntityToUserResponseDto((UserRedis) redisTemplate.opsForHash().get(USER, id));
        } else {
            var user = userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchUserIdException("There is no user with id = " + id));
            redisTemplate.opsForHash().put(USER, id, userMapper.entityToRedisEntity(user));
            return userMapper.entityToUserResponseDto(user);
        }
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
    public void deleteById(Long id) {
        if (TRUE.equals(redisTemplate.opsForHash().hasKey(USER, id))) {
            redisTemplate.opsForHash().delete(USER, id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User user = userRepository.save(userMapper.userRequestDtoToEntity(userRequestDto));
        return userMapper.entityToUserResponseDto(user);
    }

    @Override
    public UserResponseDto update(UserRequestDto userRequestDto, Long id) {
        User user = userMapper.userRequestDtoToEntity(userRequestDto);
        user.setId(id);
        return userMapper.entityToUserResponseDto(userRepository.save(user));
    }
}
