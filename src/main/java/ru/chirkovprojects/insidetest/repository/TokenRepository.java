package ru.chirkovprojects.insidetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chirkovprojects.insidetest.entity.Token;
import java.util.Date;
import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findAllByExpiredDateBefore(Date date);

    List<Token> findAllByValue(String value);

}
