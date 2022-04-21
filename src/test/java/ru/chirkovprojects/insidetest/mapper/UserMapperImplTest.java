package ru.chirkovprojects.insidetest.mapper;

import org.junit.jupiter.api.Test;
import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.dto.UserResponse;
import ru.chirkovprojects.insidetest.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperImplTest {

    UserMapper userMapper;
    User user;
    UserResponse userResponse;
    UserRequest userRequest;

    {
        userMapper = new UserMapperImpl();

        user = new User();
        user.setId(1L);
        user.setUsername("Alex");
        user.setPassword("pass");

        userRequest = new UserRequest();
        userRequest.setId(1L);
        userRequest.setUsername("Alex");
        userRequest.setPassword("pass");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("Alex");
    }

    @Test()
    void mapEntityToDtoShouldReturnNullIfArgumentsEntityIsNull() {
        assertThat(userMapper.mapEntityToDto(null)).isNull();
    }

    @Test()
    void mapDtoToEntityShouldReturnNullIfArgumentsDtoIsNull() {
        assertThat(userMapper.mapDtoToEntity(null)).isNull();
    }

    @Test()
    void mapEntityToDtoShouldReturnDtoIfArgumentIsEntity() {
        assertThat(userResponse).isEqualTo(userMapper.mapEntityToDto(user));
    }

    @Test()
    void mapDtoToEntityShouldReturnEntityIfArgumentIsDto() {
        assertThat(user).isEqualTo(userMapper.mapDtoToEntity(userRequest));
    }

}
