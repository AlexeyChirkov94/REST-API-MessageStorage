package ru.chirkovprojects.insidetest.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class MessageResponse {

    private Long id;
    private LocalDateTime dateTimeOfMessage;
    private String value;
    private Long authorId;
    private String authorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeOfMessage() {
        return dateTimeOfMessage;
    }

    public void setDateTimeOfMessage(LocalDateTime dateTimeOfMessage) {
        this.dateTimeOfMessage = dateTimeOfMessage;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageResponse that = (MessageResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dateTimeOfMessage, that.dateTimeOfMessage) &&
                Objects.equals(value, that.value) &&
                Objects.equals(authorId, that.authorId) &&
                Objects.equals(authorName, that.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTimeOfMessage, value, authorId, authorName);
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "id=" + id +
                ", dateTimeOfMessage=" + dateTimeOfMessage +
                ", value='" + value + '\'' +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                '}';
    }

}
