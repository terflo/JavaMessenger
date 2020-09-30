package com.company.app.client;

import com.company.app.network.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class EventController implements ConnectionListener {


    private RegisterRequest lastRegisterRequest;
    private LoginRequest lastLoginRequest;
    private LoginMenuController loginController;
    private RegistrationMenuController regController;
    private MainMenuController mainController;
    private final JSONParser parser;
    private volatile boolean isConnected;
    private Connection connection;
    private final Thread connectionThread;


    public EventController(LoginMenuController loginController) {


        this.isConnected = false;
        this.loginController = loginController;
        this.loginController.setEventController(this);
        this.parser = new JSONParser();


        connectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if (!isConnected) {
                        try {
                            connection = new Connection(EventController.this, Constants.IP, Constants.PORT);
                            isConnected = true;
                        } catch (IOException e) {
                            synchronized (connectionThread) {
                                try {
                                    connectionThread.wait(5000);
                                } catch (InterruptedException interruptedException) {
                                    //interruptedException.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
        connectionThread.start();
    }

    public void sendRequest(Request request) {

        if (request instanceof LoginRequest) {lastLoginRequest = (LoginRequest) request;}
        else if (request instanceof RegisterRequest) {lastRegisterRequest = (RegisterRequest) request;}

        try {
            if (connection != null) {
                connection.sendRequest(parser.convertToString(request));
            }
        } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnect(Connection connection) {
        if (loginController != null)
            loginController.greenConnectStatus();
        try {
            connection.setKey();
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            onException(connection, e);
        }
    }

    @Override
    public void onReciveString(Connection connection, String message) {

        //System.out.println("Получено сообщение " + message);
        Request request = null;

        try {
        request = parser.convertToRequest(message);
    } catch (IOException e) {
        e.printStackTrace();
    }

        switch (Objects.requireNonNull(request).getClass().getSimpleName())
    {
        case "LoginRequest":
            LoginRequest loginRequest = (LoginRequest) request;
            try {
                loginController.loginSuccessful((loginRequest.getLogin().equals("true") && loginRequest.getPassword().equals(lastLoginRequest.getPassword())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        case "RegisterRequest":
            RegisterRequest registerRequest = (RegisterRequest) request;
            regController.registrationAccept((registerRequest.getLogin().equals("true") && registerRequest.getPassword().equals(lastRegisterRequest.getPassword())));
            break;
        case "MessageRequest":
            if (this.mainController != null) {
                MessageRequest messageRequest = (MessageRequest) request;
                this.mainController.acceptMessage(messageRequest.getLogin() + ": " + messageRequest.getMessage());
            }
            break;
        default:

            break;
    }
}

    @Override
    public void onDisconnect(Connection connection) {
        loginController.redConnectStatus();
        isConnected = false;
    }

    @Override
    public void onException(Connection connection, Exception e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An unexpected error occurred");
            alert.setContentText(e.toString());
            alert.setResizable(false);
            alert.show();
        });
    }

    public void setLoginController(LoginMenuController loginController) {
        this.loginController = loginController;
    }

    public void setMainController(MainMenuController mainController) {
        this.mainController = mainController;
    }

    public void setRegController(RegistrationMenuController regController) {
        this.regController = regController;
    }

    public LoginMenuController getLoginController() {
        return loginController;
    }

    public MainMenuController getMainController() {
        return mainController;
    }

    public RegistrationMenuController getRegController() {
        return regController;
    }

    public String getConnectionLogin() {
        return this.connection.getLogin();
    }

    public void setConnectionLogin() {
        this.connection.setLogin(lastLoginRequest.getLogin());
    }
}
