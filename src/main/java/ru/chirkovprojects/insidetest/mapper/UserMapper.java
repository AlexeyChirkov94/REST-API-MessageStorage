package ru.chirkovprojects.insidetest.mapper;

import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.dto.UserResponse;
import ru.chirkovprojects.insidetest.entity.User;

public interface UserMapper {

    UserResponse mapEntityToDto(User user);

    User mapDtoToEntity(UserRequest userRequest);

}
