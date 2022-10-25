package com.example.atlas;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private double xOffset;
    private double yOffset;

    public static double countLimit;

    public static double statisticsLimit;
    public static boolean statisticsLimitSet = false;

    private Pane countPane;
    private Pane statisticsPane;
    private Pane settingsPane;

    @FXML
    private AnchorPane topBar;
    @FXML
    private AnchorPane ap;
    @FXML
    private Button countButton;
    @FXML
    private Button statisticsButton;
    @FXML
    private Button autoClickerButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Pane panelStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = MainWindow.newStage.getX() - event.getScreenX();
                yOffset = MainWindow.newStage.getY() - event.getScreenY();
            }
        });

        topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MainWindow.newStage.setX(event.getScreenX() + xOffset);
                MainWindow.newStage.setY(event.getScreenY() + yOffset);
            }
        });
    }

    @FXML
    private void handleClicks(ActionEvent event) throws IOException {

        if(event.getSource() == countButton){
            countPane =  FXMLLoader.load(getClass().getResource("count-pane.fxml"));
            panelStatus.getChildren().add(countPane);

            countButton.setDisable(true);
            statisticsButton.setDisable(false);
            settingsButton.setDisable(false);
        }
        else if(event.getSource() == statisticsButton){
            statisticsPane =  FXMLLoader.load(getClass().getResource("statistics-pane.fxml"));
            panelStatus.getChildren().add(statisticsPane);

            countButton.setDisable(false);
            statisticsButton.setDisable(true);
            settingsButton.setDisable(false);
        }
        else if(event.getSource() == autoClickerButton){
            new AutoClicker();
        }
        else if(event.getSource() == settingsButton){
            settingsPane =  FXMLLoader.load(getClass().getResource("settings-pane.fxml"));
            panelStatus.getChildren().add(settingsPane);

            countButton.setDisable(false);
            statisticsButton.setDisable(false);
            settingsButton.setDisable(true);
        }
        else if(event.getSource() == logOutButton){
            Stage oldStage = (Stage) ap.getScene().getWindow();

            oldStage.hide();

            FXMLLoader fxmlLoader = new FXMLLoader(LoginScreen.class.getResource("login-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 300);

            Stage stage = new Stage();

            stage.setTitle("Atlas");
            stage.resizableProperty().setValue(false);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(scene);

            stage.show();
        }

    }

    @FXML
    public void minimizeClick(){
        MainWindow.newStage.setIconified(true);
    }

    @FXML
    public void closeClick(){
        System.exit(0);
    }

}
