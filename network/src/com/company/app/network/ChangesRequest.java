package com.company.app.network;

public class ChangesRequest implements Request {
    private String login;
    private String password;
    private String email;

    public ChangesRequest(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public ChangesRequest() {}
}
