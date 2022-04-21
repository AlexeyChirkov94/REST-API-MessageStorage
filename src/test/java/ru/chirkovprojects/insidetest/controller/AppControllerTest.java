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
import ru.chirkovprojects.insidetest.dto.LoginResponse;
import ru.chirkovprojects.insidetest.dto.MessageRequest;
import ru.chirkovprojects.insidetest.dto.MessageResponse;
import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.dto.UserResponse;
import ru.chirkovprojects.insidetest.service.MessageService;
import ru.chirkovprojects.insidetest.service.TokenService;
import ru.chirkovprojects.insidetest.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ControllerTestsContextConfiguration.class})
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class AppControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    TokenService tokenService;

    MockMvc mockMvc;

    AppController appController;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
        Mockito.reset(messageService);
        Mockito.reset(tokenService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AppController(userService, messageService, tokenService))
                .build();
        appController = webApplicationContext.getBean(AppController.class);
    }

    @Test
    void showAllShouldReturnAllUsers() throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("Bob");
        when(userService.findAll()).thenReturn(Collections.singletonList(userResponse));

        mockMvc.perform(get("/api/user"))
                .andExpect(status().is(200))
        .andExpect(content().json("[{\"id\": 1,\"username\":\"Bob\"}]"));
    }

    @Test
    void showUserShouldReturnUser() throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("Bob");
        when(userService.findById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().is(200))
                .andExpect(content().json("{\"id\": 1,\"username\":\"Bob\"}"));
    }

    @Test
    void addUserShouldAddUser() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("Alex");
        userRequest.setPassword("pass123");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("Alex");

        when(userService.create(userRequest)).thenReturn(userResponse);

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"username\": \"Alex\",\"password\": \"pass123\"}"))
                .andExpect(status().is(200))
                .andExpect(content().json("{\"id\": 1,\"username\": \"Alex\"}"));
    }

    @Test
    void loginShouldReturnToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName("Alex");
        loginRequest.setPassword("pass");

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("tokenValue");

        when(userService.login(loginRequest)).thenReturn(loginResponse);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\": \"Alex\",\"password\": \"pass\"}"))
                .andExpect(status().is(200))
                .andExpect(content().json("{\"token\": \"tokenValue\"}"));
    }

    @Test
    void readMessageHistoryShouldReadMessagesFromDB() throws Exception {
        String authorizationValue = "Bearer_eyJ0eXAi..";

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("history 1");

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(1L);
        messageResponse.setDateTimeOfMessage(LocalDateTime.of(LocalDate.of(2022, 4, 1), LocalTime.of(14,6)));
        messageResponse.setValue("hello!");
        messageResponse.setAuthorId(3L);
        messageResponse.setAuthorName("Ivan");

        when(tokenService.checkTokenAndReturnOwnerId(authorizationValue)).thenReturn(3L);
        when(messageService.readLastMessages(messageRequest, 3L)).thenReturn(Collections.singletonList(messageResponse));

        mockMvc.perform(get("/api/message")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", authorizationValue)
                .content("{\"name\": \"Ivan\",\"message\": \"history 1\"}"))
                .andExpect(status().is(200))
                .andExpect(content().json("\t[{\n" +
                        "        \"id\": 1,\n" +
                        "        \"dateTimeOfMessage\": [\n" +
                        "            2022,\n" +
                        "            4,\n" +
                        "            1,\n" +
                        "            14,\n" +
                        "            6\n" +
                        "        ],\n" +
                        "        \"value\": \"hello!\",\n" +
                        "        \"authorId\": 3,\n" +
                        "        \"authorName\": \"Ivan\"\n" +
                        "    }]"));
    }

    @Test
    void addMessageHistoryShouldAddMessageToDB() throws Exception {
        String authorizationValue = "Bearer_eyJ0eXAi..";

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("history 1");

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(1L);
        messageResponse.setDateTimeOfMessage(LocalDateTime.of(LocalDate.of(2022, 4, 1), LocalTime.of(14,6)));
        messageResponse.setValue("hello!");
        messageResponse.setAuthorId(3L);
        messageResponse.setAuthorName("Ivan");

        when(tokenService.checkTokenAndReturnOwnerId(authorizationValue)).thenReturn(3L);
        when(messageService.create(messageRequest, 3L)).thenReturn(messageResponse);

        mockMvc.perform(post("/api/message")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", authorizationValue)
                .content("{\"name\": \"Ivan\",\"message\": \"history 1\"}"))
                .andExpect(status().is(200))
                .andExpect(content().json("{\n" +
                        "        \"id\": 1,\n" +
                        "        \"dateTimeOfMessage\": [\n" +
                        "            2022,\n" +
                        "            4,\n" +
                        "            1,\n" +
                        "            14,\n" +
                        "            6\n" +
                        "        ],\n" +
                        "        \"value\": \"hello!\",\n" +
                        "        \"authorId\": 3,\n" +
                        "        \"authorName\": \"Ivan\"\n" +
                        "    }"));
    }

}
