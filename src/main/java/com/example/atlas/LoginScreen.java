package com.example.atlas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginScreen extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("login-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);

        stage.setTitle("Atlas");
        stage.resizableProperty().setValue(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}