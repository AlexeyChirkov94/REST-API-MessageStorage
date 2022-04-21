package ru.chirkovprojects.insidetest.dto;

import java.util.Objects;

public class LoginRequest {

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest loginRequest = (LoginRequest) o;
        return Objects.equals(name, loginRequest.name) &&
                Objects.equals(password, loginRequest.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }

    @Override
    public String toString() {
        return "Login{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
