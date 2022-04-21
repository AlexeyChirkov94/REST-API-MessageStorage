package ru.chirkovprojects.insidetest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.chirkovprojects.insidetest.dto.LoginRequest;
import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.dto.UserResponse;
import ru.chirkovprojects.insidetest.entity.User;
import ru.chirkovprojects.insidetest.mapper.UserMapper;
import ru.chirkovprojects.insidetest.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenService tokenService;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void loginShouldReturnTokenIfCredentialsValid() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("encoded pass");
        List<User> users = Collections.singletonList(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName("Ivan");
        loginRequest.setPassword("pass123");

        when(userRepository.findAllByUsername("Ivan")).thenReturn(users);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);

        userService.login(loginRequest);

        verify(userRepository).findAllByUsername("Ivan");
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
    }

    @Test
    void loginShouldThrowExceptionIfNoUsersWithWantedName() {
        List<User> users = Collections.emptyList();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName("Ivan");
        loginRequest.setPassword("pass123");

        when(userRepository.findAllByUsername("Ivan")).thenReturn(users);

        assertThatThrownBy(() -> userService.login(loginRequest)).hasMessage("Credentials is invalid");

        verify(userRepository).findAllByUsername("Ivan");
    }

    @Test
    void loginShouldThrowExceptionIfCredentialsIsInvalid() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("encoded pass");
        List<User> users = Collections.singletonList(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName("Ivan");
        loginRequest.setPassword("pass123");

        when(userRepository.findAllByUsername("Ivan")).thenReturn(users);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.login(loginRequest)).hasMessage("Credentials is invalid");

        verify(userRepository).findAllByUsername("Ivan");
        verify(passwordEncoder).matches(loginRequest.getPassword(), user.getPassword());
    }

    @Test
    void createShouldAddUserToDBIfArgumentIsUserRequest() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("pass");
        List<User> users = Collections.emptyList();

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("Ivan");
        userRequest.setPassword("encoded pass");

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("Ivan");

        when(userRepository.findAllByUsername("Ivan")).thenReturn(users);
        when(passwordEncoder.encode("encoded pass")).thenReturn("encoded pass");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapEntityToDto(user)).thenReturn(userResponse);
        when(userMapper.mapDtoToEntity(userRequest)).thenReturn(user);

        userService.create(userRequest);

        verify(userRepository).findAllByUsername("Ivan");
        verify(passwordEncoder).encode("encoded pass");
        verify(userRepository).save(user);
        verify(userMapper).mapEntityToDto(user);
        verify(userMapper).mapDtoToEntity(userRequest);
    }

    @Test
    void createShouldThrowExceptionIfUserWithSameNameAlreadyExist() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("Ivan");
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("pass");
        List<User> users = Collections.singletonList(user);

        when(userRepository.findAllByUsername("Ivan")).thenReturn(users);

        assertThatThrownBy(() -> userService.create(userRequest)).hasMessage("User with same username already registered");

        verify(userRepository).findAllByUsername("Ivan");
    }

    @Test
    void findByIdShouldReturnUserFromDBIfArgumentsAreUserId() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("Ivan");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.mapEntityToDto(user)).thenReturn(userResponse);

        userService.findById(1L);

        verify(userRepository).findById(1L);
        verify(userMapper).mapEntityToDto(user);
    }

    @Test
    void findByIdShouldThrowExceptionIfNoUserWithWantedIdInDB() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(100L)).hasMessage("There no user with id: 100");

        verify(userRepository).findById(100L);
    }

    @Test
    void findAllShouldReturnListOfAllUsersFromDB() {
        User user = new User();
        user.setUsername("Vlad");

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("Vlad");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.mapEntityToDto(user)).thenReturn(userResponse);

        userService.findAll();

        verify(userRepository).findAll();
        verify(userMapper).mapEntityToDto(user);
    }

}
