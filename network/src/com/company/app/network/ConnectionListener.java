package com.company.app.network;

public interface ConnectionListener {
    void onConnect(Connection connection);
    void onReciveString(Connection connection, String message);
    void onDisconnect(Connection connection);
    void onException(Connection connection, Exception e);
}
