package com.example.atlas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class CountPaneController implements Initializable {

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private Pane pane;

    private Button countButton;
    private Button statisticsButton;
    private Button autoClickerButton;
    private Button settingsButton;

    @FXML
    private Label dateLabel;

    @FXML
    private Label wallTimeLabel;
    @FXML
    private Label wallReworkTimeLabel;
    @FXML
    private Label stagingTimeLabel;
    @FXML
    private Label stagingReworkTimeLabel;
    @FXML
    private Label photoshopTimeLabel;
    @FXML
    private Label photoshopReworkTimeLabel;

    @FXML
    private Label wallLabel;
    @FXML
    private Label wallReworkLabel;
    @FXML
    private Label stagingLabel;
    @FXML
    private Label stagingReworkLabel;
    @FXML
    private Label photoshopLabel;
    @FXML
    private Label photoshopReworkLabel;

    @FXML
    private Label wallEditsLabel;
    @FXML
    private Label wallReworkEditsLabel;
    @FXML
    private Label stageEditsLabel;
    @FXML
    private Label stageReworkEditsLabel;
    @FXML
    private Label photoshopEditsLabel;
    @FXML
    private Label photoshopReworkEditsLabel;
    @FXML
    private Label totalEditsLabel;

    @FXML
    private Button decrementWall;
    @FXML
    private Button decrementWallRework;
    @FXML
    private Button decrementStaging;
    @FXML
    private Button decrementStagingRework;
    @FXML
    private Button decrementPhotoshop;
    @FXML
    private Button decrementPhotoshopRework;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateLabel.setText(LoginScreenController.date);
        changeLabels();

        Scene scene = MainWindow.newStage.getScene();

        countButton = (Button) scene.lookup("#countButton");
        statisticsButton = (Button) scene.lookup("#statisticsButton");
        autoClickerButton = (Button) scene.lookup("#autoClickerButton");
        settingsButton = (Button) scene.lookup("#settingsButton");

        countButton.setDisable(true);

        pane = (Pane) scene.lookup("#panelStatus");

        scene.setOnKeyPressed(event -> {
            try {
                switch (event.getCode()) {
                    case DIGIT2:
                        switchToStatisticsPane();
                        break;
                    case DIGIT3:
                        openAutoClicker();
                        break;
                    case DIGIT4:
                        switchToSettingsPane();
                        break;
                    case W:
                        incrementWallClick();
                        break;
                    case S:
                        incrementStageClick();
                        break;
                    case A:
                        incrementPhotoshopClick();
                        break;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void checkIfZero(){
        if(LoginScreenController.databaseData.getWallsDone()==0)
            decrementWall.setDisable(true);
        else
            decrementWall.setDisable(false);

        if(LoginScreenController.databaseData.getWallsReworkDone()==0)
            decrementWallRework.setDisable(true);
        else
            decrementWallRework.setDisable(false);

        if(LoginScreenController.databaseData.getStagingsDone()==0)
            decrementStaging.setDisable(true);
        else
            decrementStaging.setDisable(false);

        if(LoginScreenController.databaseData.getStagingsReworkDone()==0)
            decrementStagingRework.setDisable(true);
        else
            decrementStagingRework.setDisable(false);

        if(LoginScreenController.databaseData.getPhotoshopsDone()==0)
            decrementPhotoshop.setDisable(true);
        else
            decrementPhotoshop.setDisable(false);

        if(LoginScreenController.databaseData.getPhotoshopsReworkDone()==0)
            decrementPhotoshopRework.setDisable(true);
        else
            decrementPhotoshopRework.setDisable(false);
    }

    public void updateData(){

        Document filter = new Document("Date",LoginScreenController.date);

        Document doc = (Document) MyMongoDatabase.collectionData.find(filter).first();

        if(doc != null){
            Document update = new Document("$set",new Document()
                    .append("Walls",LoginScreenController.databaseData.getWallsDone())
                    .append("Wall Reworks",LoginScreenController.databaseData.getWallsReworkDone())
                    .append("Staging",LoginScreenController.databaseData.getStagingsDone())
                    .append("Staging Reworks",LoginScreenController.databaseData.getStagingsReworkDone())
                    .append("Photoshops",LoginScreenController.databaseData.getPhotoshopsDone())
                    .append("Photoshop Reworks",LoginScreenController.databaseData.getPhotoshopsReworkDone())
            );

            MyMongoDatabase.collectionData.updateOne(filter,update);
        }

    }

    public void changeLabels(){

        LoginScreenController.databaseData.calculateAll();

        wallLabel.setText(String.format("%d",LoginScreenController.databaseData.getWallsDone()));
        wallReworkLabel.setText(String.format("%d",LoginScreenController.databaseData.getWallsReworkDone()));
        stagingLabel.setText(String.format("%d",LoginScreenController.databaseData.getStagingsDone()));
        stagingReworkLabel.setText(String.format("%d",LoginScreenController.databaseData.getStagingsReworkDone()));
        photoshopLabel.setText(String.format("%d",LoginScreenController.databaseData.getPhotoshopsDone()));
        photoshopReworkLabel.setText(String.format("%d",LoginScreenController.databaseData.getPhotoshopsReworkDone()));

        wallEditsLabel.setText(String.format("%.2f",LoginScreenController.databaseData.getWallEditsDone()));
        wallReworkEditsLabel.setText(String.format("%.2f",LoginScreenController.databaseData.getWallReworkEditsDone()));
        stageEditsLabel.setText(String.format("%.2f",LoginScreenController.databaseData.getStagingEditsDone()));
        stageReworkEditsLabel.setText(String.format("%.2f",LoginScreenController.databaseData.getStagingReworkEditsDone()));
        photoshopEditsLabel.setText(String.format("%.2f",LoginScreenController.databaseData.getPhotoshopEditsDone()));
        photoshopReworkEditsLabel.setText(String.format("%.2f",LoginScreenController.databaseData.getPhotoshopReworkEditsDone()));

        totalEditsLabel.setText(String.format("%.2f",LoginScreenController.databaseData.getTotalEdits()));

        if(LoginScreenController.databaseData.getTotalEdits()<MainWindowController.countLimit){
            totalEditsLabel.setStyle("-fx-background-color:#FF3B28;" +
                                     "-fx-border-radius:5 5 5 5;" +
                                     "-fx-background-radius: 5 5 5 5;" +
                                     "-fx-font-weight: bold;" +
                                     "-fx-font-size: 18;");
        }
        else if(LoginScreenController.databaseData.getTotalEdits()>MainWindowController.countLimit){
            totalEditsLabel.setStyle("-fx-background-color:#48EB12;" +
                                     "-fx-border-radius:5 5 5 5;" +
                                     "-fx-background-radius: 5 5 5 5;" +
                                     "-fx-font-weight: bold;" +
                                     "-fx-font-size: 18;");
        }
        else {
            totalEditsLabel.setStyle("-fx-background-color:#F0BE0C;" +
                                     "-fx-border-radius:5 5 5 5;" +
                                     "-fx-background-radius: 5 5 5 5;" +
                                     "-fx-font-weight: bold;" +
                                     "-fx-font-size: 18;");
        }

        checkIfZero();
    }

    @FXML
    public void incrementWallClick() {
        LoginScreenController.databaseData.setWallsDone(LoginScreenController.databaseData.getWallsDone()+1);
        changeLabels();
        updateData();
        wallTimeLabel.setText(dateFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void incrementWallReworkClick() {
        LoginScreenController.databaseData.setWallsReworkDone(LoginScreenController.databaseData.getWallsReworkDone()+1);
        changeLabels();
        updateData();
        wallReworkTimeLabel.setText(dateFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void incrementStageClick() {
        LoginScreenController.databaseData.setStagingsDone(LoginScreenController.databaseData.getStagingsDone()+1);
        changeLabels();
        updateData();
        stagingTimeLabel.setText(dateFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void incrementStageReworkClick() {
        LoginScreenController.databaseData.setStagingsReworkDone(LoginScreenController.databaseData.getStagingsReworkDone()+1);
        changeLabels();
        updateData();
        stagingReworkTimeLabel.setText(dateFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void incrementPhotoshopClick() {
        LoginScreenController.databaseData.setPhotoshopsDone(LoginScreenController.databaseData.getPhotoshopsDone()+1);
        changeLabels();
        updateData();
        photoshopTimeLabel.setText(dateFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void incrementPhotoshopReworkClick() {
        LoginScreenController.databaseData.setPhotoshopsReworkDone(LoginScreenController.databaseData.getPhotoshopsReworkDone()+1);
        changeLabels();
        updateData();
        photoshopReworkTimeLabel.setText(dateFormat.format(Calendar.getInstance().getTime()));
    }

    @FXML
    public void decrementWallClick() {
        LoginScreenController.databaseData.setWallsDone(LoginScreenController.databaseData.getWallsDone()-1);
        changeLabels();
        updateData();
    }

    @FXML
    public void decrementWallReworkClick() {
        LoginScreenController.databaseData.setWallsReworkDone(LoginScreenController.databaseData.getWallsReworkDone()-1);
        changeLabels();
        updateData();
    }

    @FXML
    public void decrementStageClick() {
        LoginScreenController.databaseData.setStagingsDone(LoginScreenController.databaseData.getStagingsDone()-1);
        changeLabels();
        updateData();
    }

    @FXML
    public void decrementStageReworkClick() {
        LoginScreenController.databaseData.setStagingsReworkDone(LoginScreenController.databaseData.getStagingsReworkDone()-1);
        changeLabels();
        updateData();
    }

    @FXML
    public void decrementPhotoshopClick() {
        LoginScreenController.databaseData.setPhotoshopsDone(LoginScreenController.databaseData.getPhotoshopsDone()-1);
        changeLabels();
        updateData();
    }

    @FXML
    public void decrementPhotoshopReworkClick() {
        LoginScreenController.databaseData.setPhotoshopsReworkDone(LoginScreenController.databaseData.getPhotoshopsReworkDone()-1);
        changeLabels();
        updateData();
    }

    @FXML
    public void resetWallClick() {
        LoginScreenController.databaseData.setWallsDone(0);
        changeLabels();
        updateData();
    }

    @FXML
    public void resetWallReworkClick() {
        LoginScreenController.databaseData.setWallsReworkDone(0);
        changeLabels();
        updateData();
    }

    @FXML
    public void resetStageClick() {
        LoginScreenController.databaseData.setStagingsDone(0);
        changeLabels();
        updateData();
    }

    @FXML
    public void resetStageReworkClick() {
        LoginScreenController.databaseData.setStagingsReworkDone(0);
        changeLabels();
        updateData();
    }

    @FXML
    public void resetPhotoshopClick() {
        LoginScreenController.databaseData.setPhotoshopsDone(0);
        changeLabels();
        updateData();
    }

    @FXML
    public void resetPhotoshopReworkClick() {
        LoginScreenController.databaseData.setPhotoshopsReworkDone(0);
        changeLabels();
        updateData();
    }

    @FXML
    public void resetAllClick() {
        LoginScreenController.databaseData.reset();
        changeLabels();
        updateData();
    }

    public void switchToStatisticsPane() throws IOException {

        Pane newPane = FXMLLoader.load(getClass().getResource("statistics-pane.fxml"));

        pane.getChildren().add(newPane);

        countButton.setDisable(false);
        statisticsButton.setDisable(true);
        settingsButton.setDisable(false);

    }

    public void openAutoClicker() throws IOException {

        new AutoClicker();

    }

    public void switchToSettingsPane() throws IOException {

        Pane newPane = FXMLLoader.load(getClass().getResource("settings-pane.fxml"));

        pane.getChildren().add(newPane);

        countButton.setDisable(false);
        statisticsButton.setDisable(false);
        settingsButton.setDisable(true);

    }

}
