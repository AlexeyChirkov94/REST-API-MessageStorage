package ru.chirkovprojects.insidetest.service;

import org.springframework.stereotype.Service;
import ru.chirkovprojects.insidetest.dto.MessageRequest;
import ru.chirkovprojects.insidetest.dto.MessageResponse;
import ru.chirkovprojects.insidetest.entity.Message;
import ru.chirkovprojects.insidetest.entity.User;
import ru.chirkovprojects.insidetest.mapper.MessageMapper;
import ru.chirkovprojects.insidetest.repository.MessageRepository;
import ru.chirkovprojects.insidetest.repository.UserRepository;
import ru.chirkovprojects.insidetest.service.exception.EntityDontExistException;
import ru.chirkovprojects.insidetest.service.exception.InvalidTokenException;
import ru.chirkovprojects.insidetest.service.exception.ValidException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService{

    private static final String PATTERN_HISTORY_REQUEST = "history";
    private static final Integer DEFAULT_COUNT_OF_ROW = 10;

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageResponse create(MessageRequest messageRequest, Long authorId) {

        User author = checkMatchingMessageAuthorAndTokenOwnerIfOkReturnAuthor(messageRequest, authorId);

        Message message = new Message();
        message.setValue(messageRequest.getMessage());
        message.setAuthor(author);

        validate(message);
        message.setDateTimeOfMessage(LocalDateTime.now());
        return messageMapper.mapEntityToDto(messageRepository.save(message));
    }

    @Override
    public List<MessageResponse> readLastMessages(MessageRequest messageRequest, Long authorId) {

        User author = checkMatchingMessageAuthorAndTokenOwnerIfOkReturnAuthor(messageRequest, authorId);

        String requestType = messageRequest.getMessage().substring(0, PATTERN_HISTORY_REQUEST.length());

        if(!requestType.equals(PATTERN_HISTORY_REQUEST)) throw new ValidException("Your request part: " + requestType +
                " not match with " + PATTERN_HISTORY_REQUEST);

        int countOfRow = parseCountOfRow(messageRequest);

        return messageRepository.findLastMessageByAuthorId(author.getId(), countOfRow)
                .stream()
                .map(messageMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse findById(Long id) {
        return messageMapper.mapEntityToDto(messageRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no message with id: " + id)));
    }

    @Override
    public List<MessageResponse> findAll() {

        return messageRepository.findAll()
                .stream()
                .map(messageMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private void validate(Message message){
        if(message.getValue() == null){
            throw new ValidException("Message is empty");
        }
    }

    private User checkMatchingMessageAuthorAndTokenOwnerIfOkReturnAuthor(MessageRequest messageRequest, Long authorId){

        String username = messageRequest.getName();
        List<User> authors = userRepository.findAllByUsername(username);
        if (authors.isEmpty()) throw new EntityDontExistException("There no user with username: " + username);
        if (!authors.get(0).getId().equals(authorId)) throw new InvalidTokenException("Access denied. Name of author and token don`t match");

        return authors.get(0);
    }

    private int parseCountOfRow(MessageRequest messageRequest){
        try {
            String requestCountOfRow = messageRequest.getMessage().substring(PATTERN_HISTORY_REQUEST.length() + 1);
            final int integerPage = Integer.parseInt(requestCountOfRow);
            if (integerPage <= 0) return DEFAULT_COUNT_OF_ROW;
            return integerPage;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return DEFAULT_COUNT_OF_ROW;
        }
    }

}
