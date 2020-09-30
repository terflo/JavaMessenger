package com.company.app.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import com.company.app.network.LoginRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginMenuController {

    private EventController eventController;

    @FXML
    private Label noConnection;

    @FXML
    private Label incorrectLoginPassword;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Circle connectStatus;

    @FXML
    private Label labelEmptyFields;

    @FXML
    private Button registrationButton;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;

    public LoginMenuController() {}

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    @FXML
    void initialize() {

        assert connectStatus != null : "fx:id=\"connectStatus\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert labelEmptyFields != null : "fx:id=\"labelEmptyFields\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert registrationButton != null : "fx:id=\"registrationButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginField != null : "fx:id=\"loginField\" was not injected: check your FXML file 'LoginMenu.fxml'.";


        this.noConnection.setVisible(false);
        this.labelEmptyFields.setVisible(false);
        this.incorrectLoginPassword.setVisible(false);


        registrationButton.setOnAction(actionEvent -> {
            if (!this.connectStatus.getFill().equals(Color.RED)) {
                Stage oldStage = (Stage) this.registrationButton.getScene().getWindow();
                oldStage.hide();

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("fxmls/RegistrationMenu.fxml"));

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.eventController.setRegController(fxmlLoader.getController());
                this.eventController.getRegController().setEventController(this.eventController);
                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("img/icon.png")));
                stage.initOwner(this.loginButton.getScene().getWindow());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOnCloseRequest(windowEvent -> {
                    //eventController.closeConnection();
                    System.exit(1);
                });
                stage.setTitle("Registration");
                stage.setResizable(false);
                stage.show();
            } else {
                this.incorrectLoginPassword.setVisible(false);
                this.labelEmptyFields.setVisible(false);
                this.noConnection.setVisible(true);
            }
        });

        loginButton.setOnAction(actionEvent -> {
            if (!this.connectStatus.getFill().equals(Color.RED)) {
                if (!this.passwordField.getText().equals("") && !this.loginField.getText().equals("")) {
                    eventController.sendRequest(new LoginRequest(this.loginField.getText(), this.passwordField.getText()));
                } else {
                    this.noConnection.setVisible(false);
                    this.incorrectLoginPassword.setVisible(false);
                    this.labelEmptyFields.setVisible(true);
                }
            } else {
                this.noConnection.setVisible(true);
            }
        });

    }

    public void loginSuccessful(boolean accept) throws IOException {

        if (accept) {
            this.eventController.setConnectionLogin();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage oldStage = (Stage) LoginMenuController.this.loginButton.getScene().getWindow();
                    oldStage.close();

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("fxmls/MainMenu.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    eventController.setMainController(fxmlLoader.getController());
                    eventController.getMainController().setEventController(eventController);


                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setOnCloseRequest(windowEvent -> {System.exit(1);});
                    stage.getIcons().add(new Image(Main.class.getResourceAsStream("img/icon.png")));
                    stage.setTitle("Main");
                    stage.setResizable(false);
                    stage.show();
                }
            });

        } else {
            this.noConnection.setVisible(false);
            this.labelEmptyFields.setVisible(false);
            this.incorrectLoginPassword.setVisible(true);
        }
    }

    public boolean getConnectStatus() {
        return this.connectStatus.getFill().equals(Color.GREEN);
    }

    public void greenConnectStatus() {
        this.connectStatus.setFill(Color.GREEN);
    }

    public void redConnectStatus() {
        this.connectStatus.setFill(Color.RED);
    }
}
