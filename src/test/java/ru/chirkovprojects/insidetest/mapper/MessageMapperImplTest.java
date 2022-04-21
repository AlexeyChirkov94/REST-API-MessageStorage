package ru.chirkovprojects.insidetest.mapper;

import org.junit.jupiter.api.Test;
import ru.chirkovprojects.insidetest.dto.MessageResponse;
import ru.chirkovprojects.insidetest.entity.Message;
import ru.chirkovprojects.insidetest.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.assertThat;

class MessageMapperImplTest {

    MessageMapper messageMapper;

    {
        messageMapper = new MessageMapperImpl();
    }

    @Test()
    void mapEntityToDtoShouldReturnNullIfArgumentsEntityIsNull() {
        assertThat(messageMapper.mapEntityToDto(null)).isNull();
    }

    @Test()
    void mapEntityToDtoShouldReturnEntityIfArgumentIsDtoAndNotNull() {
        User author = new User();
        author.setId(1L);
        author.setUsername("Bob");

        Message message = new Message();
        message.setId(2L);
        message.setValue("hello");
        message.setAuthor(author);
        message.setDateTimeOfMessage(LocalDateTime.of(LocalDate.of(2022, 4, 1), LocalTime.of(12,12)));

        MessageResponse expected = new MessageResponse();
        expected.setId(2L);
        expected.setValue("hello");
        expected.setAuthorId(author.getId());
        expected.setAuthorName(author.getUsername());
        expected.setDateTimeOfMessage(LocalDateTime.of(LocalDate.of(2022, 4, 1), LocalTime.of(12,12)));

        MessageResponse actual = messageMapper.mapEntityToDto(message);

        assertThat(actual).isEqualTo(expected);
    }

}
