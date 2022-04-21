package ru.chirkovprojects.insidetest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chirkovprojects.insidetest.entity.Token;
import ru.chirkovprojects.insidetest.entity.User;
import ru.chirkovprojects.insidetest.repository.TokenRepository;
import ru.chirkovprojects.insidetest.repository.UserRepository;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    TokenRepository tokenRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TokenServiceImpl tokenService;

    @Test
    void createShouldAddTokenToDBIfArgumentsAreMessageRequestAndAuthorId() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("encoded pass");

        Token token = new Token();
        token.setUser(user);
        token.setValue("token value");
        token.setExpiredDate(new Date());

        when(tokenRepository.save(token)).thenReturn(token);

        tokenService.create(token);

        verify(tokenRepository).save(token);
    }

    @Test
    void createShouldThrowExceptionIfTokenHaveNoOwner() {
        Token token = new Token();
        token.setValue("token value");
        token.setExpiredDate(new Date());

        assertThatThrownBy(() -> tokenService.create(token)).hasMessage("Some field of token not filled");
    }

    @Test
    void createShouldThrowExceptionIfTokenHaveNoValue() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("encoded pass");

        Token token = new Token();
        token.setUser(user);
        token.setExpiredDate(new Date());

        assertThatThrownBy(() -> tokenService.create(token)).hasMessage("Some field of token not filled");
    }

    @Test
    void createShouldThrowExceptionIfTokenHaveNoExpireDate() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("encoded pass");

        Token token = new Token();
        token.setUser(user);
        token.setValue("token value");

        assertThatThrownBy(() -> tokenService.create(token)).hasMessage("Some field of token not filled");
    }


    @Test
    void checkTokenAndReturnOwnerIdShouldCheckTokenAndReturnOwnerIdIfArgumentIsHeaderString() {
        String header = "Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y";
        String token_value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y";

        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("encoded pass");

        when(tokenRepository.findAllByValue(token_value)).thenReturn(Collections.singletonList(new Token()));
        when(userRepository.findAllByUsername("Petr")).thenReturn(Collections.singletonList(user));

        tokenService.checkTokenAndReturnOwnerId(header);

        verify(tokenRepository).findAllByValue(token_value);
        verify(userRepository).findAllByUsername("Petr");
    }

    @Test
    void checkTokenAndReturnOwnerIdShouldThrowExceptionIfTokenNotBrear() {
        String header = "Beareg_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y";

        assertThatThrownBy(() -> tokenService.checkTokenAndReturnOwnerId(header)).hasMessage("Access denied. Your token type are not Bearer");
    }

    @Test
    void checkTokenAndReturnOwnerIdShouldThrowExceptionIfDBNotContainToken() {
        String header = "Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y";
        String token_value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y";

        when(tokenRepository.findAllByValue(token_value)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> tokenService.checkTokenAndReturnOwnerId(header)).hasMessage("Access denied. Your token value are invalid");

        verify(tokenRepository).findAllByValue(token_value);
    }

    @Test
    void checkTokenAndReturnOwnerIdShouldThrowExceptionIfTokenNotMatchWithUser() {
        String header = "Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y";
        String token_value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y";

        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("encoded pass");

        when(tokenRepository.findAllByValue(token_value)).thenReturn(Collections.singletonList(new Token()));
        when(userRepository.findAllByUsername("Petr")).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> tokenService.checkTokenAndReturnOwnerId(header)).hasMessage("Access denied. Your token are invalid, try login again");

        verify(tokenRepository).findAllByValue(token_value);
        verify(userRepository).findAllByUsername("Petr");
    }

    @Test
    void checkTokenAndReturnOwnerIdShouldThrowExceptionIfTokenExpired() {
        String header = "Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxNjUwNDcxNzIyfQ.cAHD-t0t31CccBEnYknlhONi0tgqgjr19wpsVYiPISc";
        String token_value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxNjUwNDcxNzIyfQ.cAHD-t0t31CccBEnYknlhONi0tgqgjr19wpsVYiPISc";

        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setPassword("encoded pass");

        when(tokenRepository.findAllByValue(token_value)).thenReturn(Collections.singletonList(new Token()));
        when(userRepository.findAllByUsername("Petr")).thenReturn(Collections.singletonList(user));

        assertThatThrownBy(() -> tokenService.checkTokenAndReturnOwnerId(header)).hasMessage("Access denied. Your token expired, please login again");

        verify(tokenRepository).findAllByValue(token_value);
        verify(userRepository).findAllByUsername("Petr");
    }


    @Test
    void findByIdShouldReturnTokenFromDBIfArgumentsAreTokenId() {
        Token token = new Token();
        token.setId(1L);

        when(tokenRepository.findById(1L)).thenReturn(Optional.of(token));

        tokenService.findById(1L);

        verify(tokenRepository).findById(1L);
    }

    @Test
    void findByIdShouldThrowExceptionIfNoTokenWithWantedIdInDB() {
        when(tokenRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tokenService.findById(100L)).hasMessage("There no token with id: 100");

        verify(tokenRepository).findById(100L);
    }

    @Test
    void findAllShouldReturnListOfAllTokenFromDB() {
        Token token = new Token();
        token.setId(1L);

        when(tokenRepository.findAll()).thenReturn(Collections.singletonList(token));

        tokenService.findAll();

        verify(tokenRepository).findAll();
    }

    @Test
    void deleteByIdShouldDeleteTokenFromBD() {
        doNothing().when(tokenRepository).deleteById(1L);

        tokenService.deleteById(1L);

        verify(tokenRepository).deleteById(1L);
    }

}
