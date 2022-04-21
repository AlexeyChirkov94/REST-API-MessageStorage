package ru.chirkovprojects.insidetest.service;

import ru.chirkovprojects.insidetest.dto.MessageRequest;
import ru.chirkovprojects.insidetest.dto.MessageResponse;
import java.util.List;

public interface MessageService {

    MessageResponse create(MessageRequest messageRequest, Long authorId);

    List<MessageResponse> readLastMessages(MessageRequest messageRequest, Long authorId);

    MessageResponse findById(Long id);

    List<MessageResponse> findAll();

}
