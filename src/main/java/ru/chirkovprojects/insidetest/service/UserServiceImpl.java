package ru.chirkovprojects.insidetest.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.chirkovprojects.insidetest.dto.LoginRequest;
import ru.chirkovprojects.insidetest.dto.LoginResponse;
import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.dto.UserResponse;
import ru.chirkovprojects.insidetest.entity.Token;
import ru.chirkovprojects.insidetest.entity.User;
import ru.chirkovprojects.insidetest.mapper.UserMapper;
import ru.chirkovprojects.insidetest.repository.UserRepository;
import ru.chirkovprojects.insidetest.service.exception.EntityAlreadyExistException;
import ru.chirkovprojects.insidetest.service.exception.EntityDontExistException;
import ru.chirkovprojects.insidetest.service.exception.InvalidCredentialsException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Long TOKEN_LIFE_TIME_IN_MILLIS = 10800000L;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        List<User> users = userRepository.findAllByUsername(loginRequest.getName());
        if (users.isEmpty()) throw new InvalidCredentialsException("Credentials is invalid");
        User user = users.get(0);

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){

            Date expiredDate = new Date(System.currentTimeMillis() + TOKEN_LIFE_TIME_IN_MILLIS);
            String tokenValue = getToken(loginRequest.getName(), expiredDate);

            Token token = new Token();
            token.setValue(tokenValue);
            token.setUser(user);
            token.setExpiredDate(expiredDate);
            tokenService.create(token);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(tokenValue);
            return loginResponse;
        } else {
            throw new InvalidCredentialsException("Credentials is invalid");
        }
    }

    @Override
    public UserResponse create(UserRequest userRequest) {
        if (!userRepository.findAllByUsername(userRequest.getUsername()).isEmpty()) {
            throw new EntityAlreadyExistException("User with same username already registered");
        } else {
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            User user = userMapper.mapDtoToEntity(userRequest);
            return userMapper.mapEntityToDto(userRepository.save(user));
        }
    }

    @Override
    public UserResponse findById(Long id) {
       return userMapper.mapEntityToDto(userRepository.findById(id)
               .orElseThrow(() -> new EntityDontExistException("There no user with id: " + id)));
    }

    @Override
    public List<UserResponse> findAll() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private String getToken(String username, Date expiredDate){

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(expiredDate)
                .sign(Algorithm.HMAC256("secret"));
    }

}
