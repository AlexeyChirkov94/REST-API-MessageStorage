package ru.chirkovprojects.insidetest.controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.chirkovprojects.insidetest.service.MessageService;
import ru.chirkovprojects.insidetest.service.TokenService;
import ru.chirkovprojects.insidetest.service.UserService;

@Configuration
@ComponentScan(basePackages = "ru.chirkovprojects.insidetest.controller")
public class ControllerTestsContextConfiguration {

    @Bean
    public MessageService messageService(){
        return Mockito.mock(MessageService.class);
    }

    @Bean
    public TokenService tokenService() {
        return Mockito.mock(TokenService.class);
    }

    @Bean
    public UserService userService(){
        return Mockito.mock(UserService.class);
    }
}
