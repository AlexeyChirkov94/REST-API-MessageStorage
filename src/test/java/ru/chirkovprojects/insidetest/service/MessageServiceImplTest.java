package ru.chirkovprojects.insidetest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chirkovprojects.insidetest.dto.MessageRequest;
import ru.chirkovprojects.insidetest.dto.MessageResponse;
import ru.chirkovprojects.insidetest.entity.Message;
import ru.chirkovprojects.insidetest.entity.User;
import ru.chirkovprojects.insidetest.mapper.MessageMapper;
import ru.chirkovprojects.insidetest.repository.MessageRepository;
import ru.chirkovprojects.insidetest.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    MessageRepository messageRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MessageMapper messageMapper;

    @InjectMocks
    MessageServiceImpl messageService;

    @Test
    void createShouldAddMessageToDBIfArgumentsAreMessageRequestAndAuthorId() {
        User author = new User();
        author.setId(1L);
        author.setUsername("Ivan");
        author.setPassword("pass123");
        List<User> authors = Collections.singletonList(author);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("Hello world!");

        Message message = new Message();
        message.setValue(messageRequest.getMessage());
        message.setAuthor(author);
        message.setDateTimeOfMessage(LocalDateTime.now());

        MessageResponse messageResponse = new MessageResponse();

        when(userRepository.findAllByUsername("Ivan")).thenReturn(authors);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(messageMapper.mapEntityToDto(message)).thenReturn(messageResponse);

        messageService.create(messageRequest, 1L);

        verify(userRepository).findAllByUsername("Ivan");
        verify(messageRepository).save(any(Message.class));
        verify(messageMapper).mapEntityToDto(message);
    }

    @Test
    void createShouldThrowExceptionIfDBNotContainAuthorId() {
        User author = new User();
        author.setId(1L);
        author.setUsername("Ivan");
        author.setPassword("pass123");
        List<User> authors = Collections.emptyList();

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("Hello world!");

        when(userRepository.findAllByUsername("Ivan")).thenReturn(authors);

        assertThatThrownBy(() -> messageService.create(messageRequest, 1L)).hasMessage("There no user with username: Ivan");

        verify(userRepository).findAllByUsername("Ivan");
    }

    @Test
    void createShouldThrowExceptionIfAuthorIdFromDBAndTokenDoNotMatch() {
        User author = new User();
        author.setId(2L);
        author.setUsername("Ivan");
        author.setPassword("pass123");
        List<User> authors = Collections.singletonList(author);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("Hello world!");

        when(userRepository.findAllByUsername("Ivan")).thenReturn(authors);

        assertThatThrownBy(() -> messageService.create(messageRequest, 1L)).hasMessage("Access denied. Name of author and token don`t match");

        verify(userRepository).findAllByUsername("Ivan");
    }

    @Test
    void createShouldThrowExceptionIfSomeFieldOfMessageRequestAreEmpty() {
        User author = new User();
        author.setId(1L);
        author.setUsername("Ivan");
        author.setPassword("pass123");
        List<User> authors = Collections.singletonList(author);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");

        when(userRepository.findAllByUsername("Ivan")).thenReturn(authors);

        assertThatThrownBy(() -> messageService.create(messageRequest, 1L)).hasMessage("Message is empty");

        verify(userRepository).findAllByUsername("Ivan");
    }

    @Test
    void readLastMessagesShouldShowLastUserMessagesFromDBIfArgumentsAreMessageRequestAndAuthorId() {
        User author = new User();
        author.setId(1L);
        author.setUsername("Ivan");
        author.setPassword("pass123");
        List<User> authors = Collections.singletonList(author);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("history 1");

        Message message = new Message();
        message.setValue(messageRequest.getMessage());
        message.setAuthor(author);
        message.setDateTimeOfMessage(LocalDateTime.now());

        MessageResponse messageResponse = new MessageResponse();

        when(userRepository.findAllByUsername("Ivan")).thenReturn(authors);
        when(messageRepository.findLastMessageByAuthorId(1L, 1)).thenReturn(Collections.singletonList(message));
        when(messageMapper.mapEntityToDto(message)).thenReturn(messageResponse);

        messageService.readLastMessages(messageRequest, 1L);

        verify(userRepository).findAllByUsername("Ivan");
        verify(messageRepository).findLastMessageByAuthorId(1L, 1);
        verify(messageMapper).mapEntityToDto(message);
    }

    @Test
    void readLastMessagesShouldThrowExceptionIfArgumentAreMessageRequestInvalid() {
        User author = new User();
        author.setId(1L);
        author.setUsername("Ivan");
        author.setPassword("pass123");
        List<User> authors = Collections.singletonList(author);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("hIIstory 1");

        when(userRepository.findAllByUsername("Ivan")).thenReturn(authors);

        assertThatThrownBy(() -> messageService.readLastMessages(messageRequest, 1L)).hasMessage("Your request part: hIIstor not match with history");

        verify(userRepository).findAllByUsername("Ivan");
    }

    @Test
    void readLastMessagesShouldShowLastUserMessagesFromDBIfArgumentsAreMessageRequestHaveInvalidCountOfMessages() {
        User author = new User();
        author.setId(1L);
        author.setUsername("Ivan");
        author.setPassword("pass123");
        List<User> authors = Collections.singletonList(author);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("history ten please");

        Message message = new Message();
        message.setValue(messageRequest.getMessage());
        message.setAuthor(author);
        message.setDateTimeOfMessage(LocalDateTime.now());

        MessageResponse messageResponse = new MessageResponse();

        when(userRepository.findAllByUsername("Ivan")).thenReturn(authors);
        when(messageRepository.findLastMessageByAuthorId(1L, 10)).thenReturn(Collections.singletonList(message));
        when(messageMapper.mapEntityToDto(message)).thenReturn(messageResponse);

        messageService.readLastMessages(messageRequest, 1L);

        verify(userRepository).findAllByUsername("Ivan");
        verify(messageRepository).findLastMessageByAuthorId(1L, 10);
        verify(messageMapper).mapEntityToDto(message);
    }

    @Test
    void readLastMessagesShouldShowLastUserMessagesFromDBIfArgumentsAreMessageRequestHaveNegativeCountOfMessages() {
        User author = new User();
        author.setId(1L);
        author.setUsername("Ivan");
        author.setPassword("pass123");
        List<User> authors = Collections.singletonList(author);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setName("Ivan");
        messageRequest.setMessage("history -5");

        Message message = new Message();
        message.setValue(messageRequest.getMessage());
        message.setAuthor(author);
        message.setDateTimeOfMessage(LocalDateTime.now());

        MessageResponse messageResponse = new MessageResponse();

        when(userRepository.findAllByUsername("Ivan")).thenReturn(authors);
        when(messageRepository.findLastMessageByAuthorId(1L, 10)).thenReturn(Collections.singletonList(message));
        when(messageMapper.mapEntityToDto(message)).thenReturn(messageResponse);

        messageService.readLastMessages(messageRequest, 1L);

        verify(userRepository).findAllByUsername("Ivan");
        verify(messageRepository).findLastMessageByAuthorId(1L, 10);
        verify(messageMapper).mapEntityToDto(message);
    }

    @Test
    void findByIdShouldReturnMessagesFromDBIfArgumentsAreMessageId() {
        Message message = new Message();
        message.setValue("Hello!");
        message.setDateTimeOfMessage(LocalDateTime.now());

        MessageResponse messageResponse = new MessageResponse();

        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
        when(messageMapper.mapEntityToDto(message)).thenReturn(messageResponse);

        messageService.findById(1L);

        verify(messageRepository).findById(1L);
        verify(messageMapper).mapEntityToDto(message);
    }

    @Test
    void findByIdShouldThrowExceptionIfNoMessageWithWantedIdInDB() {
        when(messageRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.findById(100L)).hasMessage("There no message with id: 100");

        verify(messageRepository).findById(100L);
    }

    @Test
    void findAllShouldReturnListOfAllMessagesFromDB() {
        Message message = new Message();
        message.setValue("Hello!");
        message.setDateTimeOfMessage(LocalDateTime.now());

        MessageResponse messageResponse = new MessageResponse();

        when(messageRepository.findAll()).thenReturn(Collections.singletonList(message));
        when(messageMapper.mapEntityToDto(message)).thenReturn(messageResponse);

        messageService.findAll();

        verify(messageRepository).findAll();
        verify(messageMapper).mapEntityToDto(message);
    }

}
