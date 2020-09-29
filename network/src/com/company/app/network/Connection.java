package com.company.app.network;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Connection {

    private final Socket socket;
    private final Thread thread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ConnectionListener connectionlistener;
    private CryptoManager cryptoManager;
    private String login;
    private Key publicKey;

    public Connection(ConnectionListener connectionListener, String IP, int PORT) throws IOException {
        this(connectionListener, new Socket(IP, PORT));
    }


    public Connection(ConnectionListener connectionlistener, Socket socket) throws IOException {


        try {
            this.cryptoManager = new CryptoManager();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            connectionlistener.onException(this, e);
        }


        this.connectionlistener = connectionlistener;
        this.socket = socket;
        this.login = "";


        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                connectionlistener.onConnect(Connection.this);
                while(!thread.isInterrupted()){
                    try {
                        connectionlistener.onReciveString(Connection.this, in.readLine());
                    } catch (IOException e) {
                        try {
                            connectionlistener.onException(Connection.this, e);
                            disconnect();
                        } catch (IOException ioException) {
                            //System.out.println("Невозможно отключить соединение");
                        }
                    }
                }
            }
        });


        thread.start();
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public synchronized void disconnect() throws IOException {
        thread.interrupt();
        socket.close();
        connectionlistener.onDisconnect(this);
    }


    public synchronized void sendRequest(String request) throws IOException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        //System.out.println("Отправлено " + request);
        //out.write(cryptoManager.encryptString(request, this.publicKey) + "\r\n");
        out.write(request + "\r\n");
        out.flush();
    }

    public synchronized void sendKey(String str) throws IOException {
        out.write(str + "\r\n");
        out.flush();
    }

    public void setKey(Key publicKey) {
        this.publicKey = publicKey;
    }


    public Key getConnectionPublicKey() {
        return cryptoManager.getPublicKey();
    }


    @Override
    public String toString() {
        return "Connection{" +
                "socket=" + socket +
                ", thread=" + thread +
                ", in=" + in +
                ", out=" + out +
                ", connectionlistener=" + connectionlistener +
                '}';
    }
}
