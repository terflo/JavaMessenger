package com.company.app.server;

import com.company.app.network.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.ServerSocket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

public class Server implements ConnectionListener {
    private final ArrayList<MessageRequest> messages = new ArrayList<>();
    private final ArrayList<Connection> arrayList = new ArrayList<>();
    private final DBManager dbManager = new DBManager();
    private final JSONParser parser = new JSONParser();
    private ObjectOutputStream outputStream;

    public Server() {

        loadMessages();
        try {
            this.outputStream = new ObjectOutputStream(new FileOutputStream("messages.dat", true));
        } catch (IOException e) {
            System.out.println("Не удалось открыть файл для чтения сообщений");
        }

        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT);)
        {
            while (true)
            {
                try {
                    new Connection(this, serverSocket.accept());
                }
                catch (IOException eio)
                {
                    eio.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    @Override
    public synchronized void onConnect(Connection connection) {
        try {
            connection.sendKey();
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            onException(connection, e);
        }
        System.out.println("Клиент подключился!");
        arrayList.add(connection);
    }

    @Override
    public synchronized void onReciveString(Connection connection, String message) {
        System.out.println("Получил запрос " + message);
        Request request = null;

        try {
            request = parser.convertToRequest(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (Objects.requireNonNull(request).getClass().getSimpleName())
        {
            case "LoginRequest":
                try {
                    LoginRequest loginRequest = (LoginRequest) parser.convertToRequest(message);
                    if (dbManager.loginQuery(loginRequest)){
                        connection.setLogin(loginRequest.getLogin());
                        loginRequest.setLogin("true");
                        System.out.println("Клиент вошёл");
                        connection.sendRequest(parser.convertToString(loginRequest));
                        sendAllMessages(connection);
                    } else {
                        loginRequest.setLogin("false");
                        System.out.println("Клиент не вошёл");
                        connection.sendRequest(parser.convertToString(loginRequest));
                    }
                } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
                break;
            case "RegisterRequest":
                try {
                    RegisterRequest registerRequest = (RegisterRequest) parser.convertToRequest(message);
                    if (dbManager.regQuery(registerRequest)) {
                        connection.setLogin(registerRequest.getLogin());
                        registerRequest.setLogin("true");
                    } else {
                        registerRequest.setLogin("false");
                    }
                    connection.sendRequest(parser.convertToString(registerRequest));
                } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
                break;
            case "MessageRequest":
                try {
                    MessageRequest messageRequest = (MessageRequest) parser.convertToRequest(message);
                    messages.add(messageRequest);
                    rewriteMessages(messageRequest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(Connection con : arrayList) {
                    try {
                        if (!con.getLogin().equals(""))
                            con.sendRequest(message);
                    } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                System.out.println("Пришёл неопознанный запрос");
                break;
        }
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        arrayList.remove(connection);
        sendAll(connection.getLogin() + " покинул чат");
        System.out.println("Клиент отключился");
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("Ошибка " + e);
    }

    public synchronized void sendAll(String message) {
        MessageRequest messageRequest = new MessageRequest("server", message);
        messages.add(messageRequest);
        rewriteMessages(messageRequest);
        try {
            for(Connection con : arrayList) {
                con.sendRequest(parser.convertToString(new MessageRequest("Server",message)));
            }
        } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    private void loadMessages() {
        try (final ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("messages.dat"))) {
            while(true) {
                try {
                    messages.add((MessageRequest) inputStream.readObject());
                } catch (ClassNotFoundException e) {
                    break;
                }
            }
        } catch (IOException ignored) {
        }
    }

    private void rewriteMessages(MessageRequest messageRequest) {
        try {
            outputStream.writeObject(messageRequest);
            outputStream.flush();
            System.out.println("Записал " + messageRequest.getMessage());
        } catch (IOException e) {
            System.out.println("Не удалось дописать файл");
        }
    }

    private synchronized void sendAllMessages(Connection connection) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendAll(connection.getLogin() + " вошёл в чат");
                try {
                    synchronized (this) {this.wait(1000);}
                } catch (InterruptedException e) {
                    System.out.println("Не поток а нить");
                }
                for(MessageRequest messageRequest : messages) {
                    try {
                        connection.sendRequest(parser.convertToString(messageRequest));
                    } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
                        System.out.println("Не баг а фича");
                    }
                }
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
