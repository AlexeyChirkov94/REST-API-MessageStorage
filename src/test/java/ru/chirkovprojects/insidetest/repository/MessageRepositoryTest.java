package ru.chirkovprojects.insidetest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.chirkovprojects.insidetest.entity.Message;
import ru.chirkovprojects.insidetest.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class MessageRepositoryTest {

    ApplicationContext context;
    MessageRepository messageRepository;
    TokenRepository tokenRepository;
    UserRepository userRepository;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        messageRepository = context.getBean(MessageRepository.class);
        tokenRepository = context.getBean(TokenRepository.class);
        userRepository = context.getBean(UserRepository.class);
    }

    @Test
    void findLastMessageByAuthorIdShouldShodExtractLastMessageIfArgumentsAreAuthorIdAndCountOfNotes(){

        List<Message> actual = messageRepository.findLastMessageByAuthorId(1L, 2);

        User authorOfMessages = new User();
        authorOfMessages.setId(1L);
        authorOfMessages.setUsername("Alexey");
        authorOfMessages.setPassword("$2a$10$cWAicrqs0V5ge4nIaMbgjehUBIYngrXXgj6yFcBiEvW51UDFyU9Ui");

        Message message12 = new Message();
        message12.setId(12L);
        message12.setDateTimeOfMessage(LocalDateTime.of(LocalDate.of(2022, 4, 1), LocalTime.of(12,12)));
        message12.setValue("Alexey message 12");
        message12.setAuthor(authorOfMessages);

        Message message11 = new Message();
        message11.setId(11L);
        message11.setDateTimeOfMessage(LocalDateTime.of(LocalDate.of(2022, 4, 1), LocalTime.of(12,11)));
        message11.setValue("Alexey message 11");
        message11.setAuthor(authorOfMessages);

        List<Message> expected = Arrays.asList(message12, message11);

        assertThat(actual).isEqualTo(expected);
    }

}
