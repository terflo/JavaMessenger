package com.company.app.network;

public class RegisterRequest implements Request {
    private String login;
    private String password;
    private String email;

    public RegisterRequest(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public RegisterRequest() {}

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
