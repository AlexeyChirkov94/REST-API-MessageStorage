package ru.chirkovprojects.insidetest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.chirkovprojects.insidetest.dto.LoginRequest;
import ru.chirkovprojects.insidetest.dto.MessageRequest;
import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.service.MessageService;
import ru.chirkovprojects.insidetest.service.TokenService;
import ru.chirkovprojects.insidetest.service.UserService;
import ru.chirkovprojects.insidetest.service.exception.EntityAlreadyExistException;
import ru.chirkovprojects.insidetest.service.exception.EntityDontExistException;
import ru.chirkovprojects.insidetest.service.exception.InvalidCredentialsException;
import ru.chirkovprojects.insidetest.service.exception.InvalidTokenException;
import ru.chirkovprojects.insidetest.service.exception.ValidException;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ControllerTestsContextConfiguration.class})
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    TokenService tokenService;

    MockMvc mockMvc;

    GlobalExceptionHandler globalExceptionHandler;
    AppController appController;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
        Mockito.reset(messageService);
        Mockito.reset(tokenService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AppController(userService, messageService, tokenService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        globalExceptionHandler = webApplicationContext.getBean(GlobalExceptionHandler.class);
        appController = webApplicationContext.getBean(AppController.class);
    }

    @Test
    void addUserShouldShowExceptionMessageIfUserServiceRaiseException() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("Alex");
        userRequest.setPassword("pass123");
        Exception exception = new EntityAlreadyExistException("User with same username already registered");

        when(userService.create(userRequest)).thenThrow(exception);

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "    \"username\": \"Alex\",\n" +
                        "    \"password\": \"pass123\"\n" +
                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\": \"User with same username already registered\"}"));
    }

    @Test
    void showUserShouldThrowExceptionMessageIfUserNotExist() throws Exception {
        Exception exception = new EntityDontExistException("There no user with id: 100");

        when(userService.findById(100L)).thenThrow(exception);

        mockMvc.perform(get("/api/user/100"))
                .andExpect(status().is(404))
                .andExpect(content().json("{\"message\": \"There no user with id: 100\"}"));
    }

    @Test
    void loginShouldThrowExceptionMessageIfCredentialsIsInvalid() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName("Alex");
        loginRequest.setPassword("pass");
        Exception exception = new InvalidCredentialsException("Credentials is invalid");

        when(userService.login(loginRequest)).thenThrow(exception);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\": \"Alex\",\"password\": \"pass\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\": \"Credentials is invalid\"}"));
    }

    @Test
    void readMessageHistoryShouldThrowExceptionMessageIfNameOfUserAndTokenDoNotMatch() throws Exception {
        String authorizationValue = "Bearer_eyJ0eXAi..";

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("history 1");
        Exception exception = new InvalidTokenException("Access denied. Name of author and token don`t match");

        when(tokenService.checkTokenAndReturnOwnerId(authorizationValue)).thenReturn(5L);
        when(messageService.readLastMessages(messageRequest, 5L)).thenThrow(exception);

        mockMvc.perform(get("/api/message")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", authorizationValue)
                .content("{\"name\": \"Ivan\",\"message\": \"history 1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\": \"Access denied. Name of author and token don`t match\"}"));
    }

    @Test
    void readMessageHistoryShouldThrowExceptionMessageIfWordHistoryWrittenWithMistake() throws Exception {
        String authorizationValue = "Bearer_eyJ0eXAi..";

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("h1story 1");
        Exception exception = new ValidException("Your request part: h1story not match with history");

        when(tokenService.checkTokenAndReturnOwnerId(authorizationValue)).thenReturn(5L);
        when(messageService.readLastMessages(messageRequest, 5L)).thenThrow(exception);

        mockMvc.perform(get("/api/message")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", authorizationValue)
                .content("{\"name\": \"Ivan\",\"message\": \"h1story 1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\": \"Your request part: h1story not match with history\"}"));
    }

}
