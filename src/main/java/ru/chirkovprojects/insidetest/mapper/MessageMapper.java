package ru.chirkovprojects.insidetest.mapper;

import ru.chirkovprojects.insidetest.dto.MessageResponse;
import ru.chirkovprojects.insidetest.entity.Message;

public interface MessageMapper {

    MessageResponse mapEntityToDto(Message message);

}
