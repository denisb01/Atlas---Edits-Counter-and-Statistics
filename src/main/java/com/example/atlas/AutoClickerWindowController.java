package com.example.atlas;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class AutoClickerWindowController implements Initializable{

    @FXML
    private Button startButton;
    @FXML
    private Button pickLocationButton;

    @FXML
    private RadioButton repeatRadio;
    @FXML
    private RadioButton repeatUntilRadio;
    @FXML
    private TextField repeatNumberField;
    @FXML
    private TextField xNumberField;
    @FXML
    private TextField yNumberField;

    @FXML
    private TextField hourNumberField;
    @FXML
    private TextField minutesNumberField;
    @FXML
    private TextField secondsNumberField;
    @FXML
    private TextField milisecondsNumberField;

    private Timer timer;

    private Robot robot;

    private boolean set = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hourNumberField.setText("0");
        minutesNumberField.setText("0");
        secondsNumberField.setText("0");
        milisecondsNumberField.setText("0");

        repeatNumberField.setText("0");

        repeatRadio.setSelected(true);

        robot = new Robot();

    }

    @FXML
    public void pickLocation() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);

        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreenController.class.getResource("blank.fxml"));

        Scene scene = new Scene(fxmlLoader.load(),stage.getMaxWidth(),stage.getMaxHeight());
        stage.setOpacity(0.2);
        stage.setScene(scene);

        stage.setMaximized(true);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case ESCAPE:
                    stage.close();
                    break;
            }
        });

        scene.setOnMouseClicked(event -> {
            xNumberField.setText(String.valueOf(event.getScreenX()));
            yNumberField.setText(String.valueOf(event.getScreenY()));
            stage.close();
        });

        stage.show();
    }

    @FXML
    public void start() throws InterruptedException {

        if (set == false) {

            int miliseconds = Integer.parseInt(milisecondsNumberField.getText());
            int seconds = Integer.parseInt(secondsNumberField.getText());
            int minutes = Integer.parseInt(minutesNumberField.getText());
            int hours = Integer.parseInt(hourNumberField.getText());

            try {
                robot.mouseMove(Double.parseDouble(xNumberField.getText()), Double.parseDouble(yNumberField.getText()));
            }
            catch (Exception e){

            }

            startButton.setText("Stop (F6)");

            if (repeatRadio.isSelected()) {
                int repetitionNumber = Integer.parseInt(repeatNumberField.getText());

                timer = new Timer();

                set = true;

                disableOrEnable();

                TimerTask periodicTask = new TimerTask() {
                    @Override
                    public void run() {
                        Runnable run = new Runnable() {
                            static int i = 0;

                            @Override
                            public void run() {
                                robot.mousePress(MouseButton.PRIMARY);
                                robot.mouseRelease(MouseButton.PRIMARY);

                                i++;
                                if (i == repetitionNumber) {
                                    timer.cancel();
                                    timer.purge();
                                    i = 0;
                                    startButton.setText("Start (F6)");
                                    set = false;

                                    disableOrEnable();
                                }
                            }
                        };
                        Platform.runLater(run);
                    }
                };

                timer.scheduleAtFixedRate(periodicTask, 0, 3600000 * hours + 60000 * minutes + 1000 * seconds + miliseconds);

            } else if (repeatUntilRadio.isSelected()) {
                set = true;

                disableOrEnable();

                timer = new Timer();

                startButton.setText("Stop (F6)");

                TimerTask foreverTask = new TimerTask() {
                    @Override
                    public void run() {
                        Runnable run = new Runnable() {
                            @Override
                            public void run() {
                                robot.mousePress(MouseButton.PRIMARY);
                                robot.mouseRelease(MouseButton.PRIMARY);
                            }
                        };
                        Platform.runLater(run);
                    }
                };

                timer.scheduleAtFixedRate(foreverTask, 0, 3600000 * hours + 60000 * minutes + 1000 * seconds + miliseconds);
            }
        }
        else{
            timer.cancel();
            timer.purge();
            startButton.setText("Start (F6)");
            set = false;
            disableOrEnable();
        }
    }

    public void disableOrEnable(){
        if (set == true){
            hourNumberField.setDisable(true);
            minutesNumberField.setDisable(true);
            secondsNumberField.setDisable(true);
            milisecondsNumberField.setDisable(true);

            repeatRadio.setDisable(true);
            repeatUntilRadio.setDisable(true);

            repeatNumberField.setDisable(true);

            xNumberField.setDisable(true);
            yNumberField.setDisable(true);

            pickLocationButton.setDisable(true);
        }
        else {
            hourNumberField.setDisable(false);
            minutesNumberField.setDisable(false);
            secondsNumberField.setDisable(false);
            milisecondsNumberField.setDisable(false);

            repeatRadio.setDisable(false);
            repeatUntilRadio.setDisable(false);

            repeatNumberField.setDisable(false);

            xNumberField.setDisable(false);
            yNumberField.setDisable(false);

            pickLocationButton.setDisable(false);
        }
    }

}
