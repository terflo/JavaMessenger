package com.company.app.network;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Connection {

    private final Socket socket;
    private final Thread thread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ConnectionListener connectionlistener;
    private AesCryptoManager cryptoManager;
    private String login;

    public Connection(ConnectionListener connectionListener, String IP, int PORT) throws IOException {
        this(connectionListener, new Socket(IP, PORT));
    }


    public Connection(ConnectionListener connectionlistener, Socket socket) throws IOException {


        try {
            this.cryptoManager = new AesCryptoManager();
        } catch (NoSuchAlgorithmException e) {
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
                        connectionlistener.onReciveString(Connection.this, new String(cryptoManager.decryptString(cryptoManager.convertToByteCode(in.readLine()))));
                    } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
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

    public void sendKey() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        out.write(cryptoManager.convertToString(cryptoManager.getKey().getEncoded()) + "\n");
        out.flush();
    }

    public void setKey() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String str = in.readLine();
        byte[] decodedBytes = cryptoManager.convertToByteCode(str);
        cryptoManager.setKey(new SecretKeySpec(decodedBytes, 0, decodedBytes.length, "AES"));
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


    public synchronized void sendRequest(String request) throws IOException, BadPaddingException, IllegalBlockSizeException {
        String str = cryptoManager.convertToString(cryptoManager.encryptString(request));
        out.write(str + "\n");
        out.flush();
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
