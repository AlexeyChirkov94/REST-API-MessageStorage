package ru.chirkovprojects.insidetest.mapper;

import org.springframework.stereotype.Component;
import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.dto.UserResponse;
import ru.chirkovprojects.insidetest.entity.User;

@Component
public class UserMapperImpl implements UserMapper{

    @Override
    public UserResponse mapEntityToDto(User user) {
        if (user == null) {
            return null;
        } else {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            return userResponse;
        }
    }

    @Override
    public User mapDtoToEntity(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userRequest.getId());
            user.setUsername(userRequest.getUsername());
            user.setPassword(userRequest.getPassword());
            return user;
        }
    }

}
