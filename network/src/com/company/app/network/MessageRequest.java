package com.company.app.network;

import java.io.Serializable;

public class MessageRequest implements Request, Serializable {

    private String login;
    private String message;

    public MessageRequest() {}

    public MessageRequest(String login, String message) {
        this.login = login;
        this.message = message;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
