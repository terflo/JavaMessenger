package com.company.app.client;

import java.net.URL;
import java.util.ResourceBundle;

import com.company.app.network.RegisterRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationMenuController {

    private EventController eventController;

    @FXML
    private Label notUniqueLogin;

    @FXML
    private Label fillAllFieldsLabel;

    @FXML
    private Label notMatchPasswords;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordConfirmField;

    @FXML
    private Button joinButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;

    public RegistrationMenuController() {}

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    @FXML
    void initialize() {

        this.notMatchPasswords.setVisible(false);
        this.notUniqueLogin.setVisible(false);
        this.fillAllFieldsLabel.setVisible(false);

        joinButton.setOnAction(actionEvent -> {
            if (!passwordField.getText().equals("") && !loginField.getText().equals("") && !emailField.getText().equals("") && !passwordConfirmField.getText().equals("")) {
                if (passwordField.getText().trim().equals(passwordConfirmField.getText().trim())) {
                    eventController.sendRequest(new RegisterRequest(this.loginField.getText().trim(), this.passwordField.getText().trim(), this.emailField.getText().trim()));
                } else {
                    this.fillAllFieldsLabel.setVisible(false);
                    this.notUniqueLogin.setVisible(false);
                    this.notMatchPasswords.setVisible(true);
                }
            } else
            {
                this.notUniqueLogin.setVisible(false);
                this.notMatchPasswords.setVisible(false);
                this.fillAllFieldsLabel.setVisible(true);
            }
        });
    }

    public void registrationAccept(boolean accept) {
        if (accept) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage oldStage = (Stage) joinButton.getScene().getWindow();
                    oldStage.hide();
                    Stage parentStage = (Stage) oldStage.getOwner().getScene().getWindow();
                    parentStage.show();
                }
            });
        } else {
            this.fillAllFieldsLabel.setVisible(false);
            this.notMatchPasswords.setVisible(false);
            this.notUniqueLogin.setVisible(true);
        }
    }
}
