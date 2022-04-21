package ru.chirkovprojects.insidetest.mapper;

import org.springframework.stereotype.Component;
import ru.chirkovprojects.insidetest.dto.MessageResponse;
import ru.chirkovprojects.insidetest.entity.Message;

@Component
public class MessageMapperImpl implements MessageMapper{

    @Override
    public MessageResponse mapEntityToDto(Message message) {
        if (message == null) {
            return null;
        } else {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setId(message.getId());
            messageResponse.setDateTimeOfMessage(message.getDateTimeOfMessage());
            messageResponse.setValue(message.getValue());
            messageResponse.setAuthorId(message.getAuthor().getId());
            messageResponse.setAuthorName(message.getAuthor().getUsername());
            return messageResponse;
        }
    }

}
