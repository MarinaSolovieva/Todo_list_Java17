package com.example.todo.service;

import com.example.todo.dao.UserRepository;
import com.example.todo.exception_handling.exception.NoSuchUserIdException;
import com.example.todo.model.dto.UserRequestDto;
import com.example.todo.model.dto.UserResponseDto;
import com.example.todo.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto findById(Long id) {
        return convertUserToUserResponseDto(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserIdException("There is no user with id = " + id)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User user = userRepository.save(convertUserRequestDtoToUser(userRequestDto));
        return convertUserToUserResponseDto(user);
    }

    @Override
    public UserResponseDto update(UserRequestDto userRequestDto, Long id) {
        User user = convertUserRequestDtoToUser(userRequestDto);
        user.setId(id);
        return convertUserToUserResponseDto(userRepository.save(user));
    }

    private UserResponseDto convertUserToUserResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getSurname());
    }

    private User convertUserRequestDtoToUser(UserRequestDto userRequestDto) {
        return new User(userRequestDto.name(), userRequestDto.surname());
    }
}
