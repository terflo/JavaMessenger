package com.company.app.client;
import java.net.URL;
import java.util.ResourceBundle;

import com.company.app.network.MessageRequest;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MainMenuController {

        private EventController eventController;

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private TextField messageField;

        @FXML
        private Button exitButton;

        @FXML
        private Button settingsButton;

        @FXML
        private TextArea chatField;

        @FXML
        private Button profileButton;

        @FXML
        private Button sendButton;

        public MainMenuController() {}

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    @FXML
        void initialize() {
            assert messageField != null : "fx:id=\"messageField\" was not injected: check your FXML file 'MainMenu.fxml'.";
            assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
            assert settingsButton != null : "fx:id=\"settingsButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
            assert chatField != null : "fx:id=\"chatField\" was not injected: check your FXML file 'MainMenu.fxml'.";
            assert profileButton != null : "fx:id=\"profileButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
            assert sendButton != null : "fx:id=\"sendButton\" was not injected: check your FXML file 'MainMenu.fxml'.";

            this.messageField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (!MainMenuController.this.messageField.getText().trim().equals("")) {
                            MainMenuController.this.eventController.sendRequest(new MessageRequest(MainMenuController.this.eventController.getConnectionLogin(), MainMenuController.this.messageField.getText()));
                            MainMenuController.this.messageField.setText("");
                        }
                    }
                }
            });

            this.exitButton.setOnAction(actionEvent -> {
                //eventController.closeConnection();
                System.exit(1);
            });

            this.sendButton.setOnAction(actionEvent -> {
                if (!this.messageField.getText().trim().equals("")) {
                    this.eventController.sendRequest(new MessageRequest(this.eventController.getConnectionLogin(), this.messageField.getText()));
                    this.messageField.setText("");
                }
            });

        }

        public synchronized void acceptMessage(String message) {
            this.chatField.appendText(message + "\n\r");
        }
}
