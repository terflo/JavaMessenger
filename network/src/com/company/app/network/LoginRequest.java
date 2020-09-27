package com.company.app.network;

public class LoginRequest implements Request {

    private String login;
    private String password;


    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public LoginRequest() {}

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

    @Override
    public String toString() {
        return "LoginRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
