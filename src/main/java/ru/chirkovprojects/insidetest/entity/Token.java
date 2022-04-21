package ru.chirkovprojects.insidetest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="value", unique = true)
    private String value;

    @Column(name="date_of_expire")
    private Date expiredDate;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(id, token.id) &&
                Objects.equals(value, token.value) &&
                Objects.equals(expiredDate, token.expiredDate) &&
                Objects.equals(user, token.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, expiredDate, user);
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", expiredDate=" + expiredDate +
                ", user=" + user +
                '}';
    }

}
