package com.example.atlas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AutoClicker {

    public static Stage stage;

    public AutoClicker() throws IOException {
        stage = new Stage();
        stage.setTitle("AutoClicker");
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreenController.class.getResource("auto-clicker-window.fxml"));
        stage.resizableProperty().setValue(false);
        Scene scene = new Scene(fxmlLoader.load(),400,300);
        stage.setScene(scene);

        AutoClickerWindowController autoClickerWindowController = fxmlLoader.getController();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F6:
                    try {
                        autoClickerWindowController.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });

        stage.show();
    }

}
