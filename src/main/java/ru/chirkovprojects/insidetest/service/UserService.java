package ru.chirkovprojects.insidetest.service;

import ru.chirkovprojects.insidetest.dto.LoginRequest;
import ru.chirkovprojects.insidetest.dto.LoginResponse;
import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.dto.UserResponse;
import java.util.List;

public interface UserService {

    LoginResponse login(LoginRequest loginRequest);

    UserResponse create(UserRequest userRequest);

    UserResponse findById(Long id);

    List<UserResponse> findAll();

}
