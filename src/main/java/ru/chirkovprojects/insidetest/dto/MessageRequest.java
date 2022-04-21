package ru.chirkovprojects.insidetest.dto;

import java.util.Objects;

public class MessageRequest {

    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageRequest that = (MessageRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, message);
    }

    @Override
    public String toString() {
        return "MessageRequest{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
