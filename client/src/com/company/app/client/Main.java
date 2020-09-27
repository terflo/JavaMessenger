package com.company.app.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fxmls/LoginMenu.fxml"));
        Parent root = fxmlLoader.load();

        stage.setTitle("Login");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("img/icon.png")));
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest( windowEvent -> {System.exit(0);});
        stage.setResizable(false);
        stage.show();

        EventController eventController = new EventController(fxmlLoader.getController());
    }
}
