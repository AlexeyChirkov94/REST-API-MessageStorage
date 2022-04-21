package ru.chirkovprojects.insidetest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="datetime_of_message")
    private LocalDateTime dateTimeOfMessage;

    @Column(name="value")
    private String value;

    @ManyToOne
    @JoinColumn(name="author_id")
    private User author;

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

    public void setValue(String message) {
        this.value = message;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(dateTimeOfMessage, message1.dateTimeOfMessage) &&
                Objects.equals(value, message1.value) &&
                Objects.equals(author, message1.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTimeOfMessage, value, author);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", dateTimeOfMessage=" + dateTimeOfMessage +
                ", message='" + value + '\'' +
                ", author=" + author +
                '}';
    }

}
