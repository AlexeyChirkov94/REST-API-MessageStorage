package ru.chirkovprojects.insidetest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.chirkovprojects.insidetest.dto.LoginRequest;
import ru.chirkovprojects.insidetest.dto.LoginResponse;
import ru.chirkovprojects.insidetest.dto.MessageRequest;
import ru.chirkovprojects.insidetest.dto.MessageResponse;
import ru.chirkovprojects.insidetest.dto.UserRequest;
import ru.chirkovprojects.insidetest.dto.UserResponse;
import ru.chirkovprojects.insidetest.service.MessageService;
import ru.chirkovprojects.insidetest.service.TokenService;
import ru.chirkovprojects.insidetest.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    private final UserService userService;
    private final MessageService messageService;
    private final TokenService tokenService;

    public AppController(UserService userService, MessageService messageService, TokenService tokenService) {
        this.userService = userService;
        this.messageService = messageService;
        this.tokenService = tokenService;
    }

    @GetMapping("/user")
    public List<UserResponse> showAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    public UserResponse showUser(@PathVariable("id") long id){
        return userService.findById(id);
    }

    @PostMapping("/user")
    public UserResponse addUser(@RequestBody UserRequest user){
        return userService.create(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }

    @GetMapping("/message")
    public List<MessageResponse> readMessages(@RequestHeader String authorization, @RequestBody MessageRequest messageRequest){
        return messageService.readLastMessages(messageRequest, tokenService.checkTokenAndReturnOwnerId(authorization));
    }

    @PostMapping("/message")
    public MessageResponse addMessage(@RequestHeader String authorization, @RequestBody MessageRequest messageRequest){
        return messageService.create(messageRequest, tokenService.checkTokenAndReturnOwnerId(authorization));
    }

}
