package ru.chirkovprojects.insidetest.repository.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.chirkovprojects.insidetest.entity.Token;
import ru.chirkovprojects.insidetest.repository.TokenRepository;
import java.util.Date;
import java.util.List;

@Component
public class TokenRemover {

    private static final long EXPIRED_TOKEN_REMOVING_INTERVAL_IN_MILLIS = 86400000L; //24H

    private final TokenRepository tokenRepository;

    public TokenRemover(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(fixedRate = EXPIRED_TOKEN_REMOVING_INTERVAL_IN_MILLIS)
    public void removeExpiredTokens() {
        Date currentDate = new Date(System.currentTimeMillis());
        List<Token> expiredTokens = tokenRepository.findAllByExpiredDateBefore(currentDate);
        tokenRepository.deleteAllInBatch(expiredTokens);
    }

}
