package ru.chirkovprojects.insidetest.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import ru.chirkovprojects.insidetest.entity.Token;
import ru.chirkovprojects.insidetest.entity.User;
import ru.chirkovprojects.insidetest.repository.TokenRepository;
import ru.chirkovprojects.insidetest.repository.UserRepository;
import ru.chirkovprojects.insidetest.service.exception.EntityDontExistException;
import ru.chirkovprojects.insidetest.service.exception.InvalidTokenException;
import ru.chirkovprojects.insidetest.service.exception.ValidException;
import java.util.Date;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private static final String TOKEN_TYPE = "Bearer";

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public TokenServiceImpl(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Token create(Token token) {
        validate(token);
        return tokenRepository.save(token);
    }

    @Override
    public Long checkTokenAndReturnOwnerId(String header) {
        String tokenType = header.substring(0, TOKEN_TYPE.length());
        String tokenValue = header.substring(TOKEN_TYPE.length() + 1);

        if (!tokenType.equals(TOKEN_TYPE)) throw new InvalidTokenException("Access denied. Your token type are not " + TOKEN_TYPE);

        List<Token> searchingTokens = tokenRepository.findAllByValue(tokenValue);
        if (searchingTokens.isEmpty()) throw new InvalidTokenException("Access denied. Your token value are invalid");

        DecodedJWT jwt = JWT.decode(tokenValue);
        String username = jwt.getSubject();
        Date expireDate = jwt.getExpiresAt();
        List<User> users = userRepository.findAllByUsername(username);
        if (users.isEmpty())  throw new InvalidTokenException("Access denied. Your token are invalid, try login again");
        if (expireDate.before(new Date(System.currentTimeMillis())))  throw new InvalidTokenException("Access denied. Your token expired, please login again");

        return users.get(0).getId();
    }

    @Override
    public Token findById(Long id) {
        return tokenRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no token with id: " + id));
    }

    @Override
    public List<Token> findAll() {
        return tokenRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        tokenRepository.deleteById(id);
    }

    private void validate(Token token){
        if(token.getUser() == null || token.getValue() == null || token.getExpiredDate() == null){
            throw new ValidException("Some field of token not filled");
        }
    }

}
