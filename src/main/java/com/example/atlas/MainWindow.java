package com.example.atlas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainWindow {

    public static Stage newStage;
    private Pane pane;
    private Button countButton;
    private Button statisticsButton;
    private Button settingsButton;

    public MainWindow() throws IOException {

        newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreenController.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 640);
        scene.setFill(Color.TRANSPARENT);

        newStage.setTitle("Atlas");
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.resizableProperty().setValue(false);
        newStage.setScene(scene);

        Label emailLabel = (Label) scene.lookup("#userEmailLabel");
        emailLabel.setText(LoginScreenController.userEmailAddress);

        pane = (Pane) scene.lookup("#panelStatus");
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("count-pane.fxml"));
        pane.getChildren().add(newLoadedPane);

        countButton = (Button) scene.lookup("#countButton");
        countButton.setDisable(true);

        statisticsButton = (Button) scene.lookup("#statisticsButton");

        settingsButton = (Button) scene.lookup("#settingsButton");

        newStage.show();

    }

}
