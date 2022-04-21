package ru.chirkovprojects.insidetest.service;

import ru.chirkovprojects.insidetest.entity.Token;
import java.util.List;

public interface TokenService {

    Token create(Token token);

    Long checkTokenAndReturnOwnerId(String header);

    Token findById(Long id);

    List<Token> findAll();

    void deleteById(Long id);

}
