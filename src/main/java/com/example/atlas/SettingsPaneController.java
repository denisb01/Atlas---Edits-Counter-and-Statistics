package com.example.atlas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPaneController implements Initializable {

    private Pane pane;

    private Button countButton;
    private Button statisticsButton;
    private Button autoClickerButton;
    private Button settingsButton;

    private boolean limitSet;
    private boolean wallsSet;
    private boolean wallReworksSet;
    private boolean stagingSet;
    private boolean stagingReworkSet;
    private boolean photoshopSet;
    private boolean photoshopReworkSet;

    @FXML
    private TextField limitField;
    @FXML
    private Button setLimitButton;

    @FXML
    private TextField wallField;
    @FXML
    private Button setWallButton;
    @FXML
    private TextField wallReworkField;
    @FXML
    private Button setWallReworkButton;
    @FXML
    private TextField stagingField;
    @FXML
    private Button setStagingButton;
    @FXML
    private TextField stagingReworkField;
    @FXML
    private Button setStagingReworkButton;
    @FXML
    private TextField photoshopField;
    @FXML
    private Button setPhotoshopButton;
    @FXML
    private TextField photoshopReworkField;
    @FXML
    private Button setPhotoshopReworkButton;

    @FXML
    private TextField newEmailField;
    @FXML
    private TextField currentEmailField;
    @FXML
    private Button changeEmailButton;
    @FXML
    private TextField currentPasswordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private Button changePasswordButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Scene scene = MainWindow.newStage.getScene();

        countButton = (Button) scene.lookup("#countButton");
        statisticsButton = (Button) scene.lookup("#statisticsButton");
        autoClickerButton = (Button) scene.lookup("#autoClickerButton");
        settingsButton = (Button) scene.lookup("#settingsButton");

        settingsButton.setDisable(true);

        pane = (Pane) scene.lookup("#panelStatus");

        scene.setOnKeyPressed(event -> {
            try {
                switch (event.getCode()) {
                    case DIGIT1:
                        switchToCountPane();
                        break;
                    case DIGIT2:
                        switchToStatisticsPane();
                        break;
                    case DIGIT3:
                        openAutoClicker();
                        break;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        limitField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                changeOrSetLimit();
            }
        });

        wallField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                changeOrSetWalls();
            }
        });

        wallReworkField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                changeOrSetWallReworks();
            }
        });

        stagingField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                changeOrSetStagings();
            }
        });

        stagingReworkField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                changeOrSetStagingReworks();
            }
        });

        photoshopField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                changeOrSetPhotoshops();
            }
        });

        photoshopReworkField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                changeOrSetPhotoshopReworks();
            }
        });

        initialization();

    }

    public void switchToCountPane() throws IOException {

        Pane newPane = FXMLLoader.load(getClass().getResource("count-pane.fxml"));

        pane.getChildren().add(newPane);

        countButton.setDisable(true);
        statisticsButton.setDisable(false);
        settingsButton.setDisable(false);

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

    public void initialization(){
        limitField.setText(String.valueOf(MainWindowController.countLimit));
        limitField.setDisable(true);
        setLimitButton.setText("Change");

        wallField.setText(String.valueOf(ValuesData.wallCoefficient));
        wallField.setDisable(true);
        setWallButton.setText("Change");

        wallReworkField.setText(String.valueOf(ValuesData.wallReworkCoefficient));
        wallReworkField.setDisable(true);
        setWallReworkButton.setText("Change");

        stagingField.setText(String.valueOf(ValuesData.stagingCoefficient));
        stagingField.setDisable(true);
        setStagingButton.setText("Change");

        stagingReworkField.setText(String.valueOf(ValuesData.stagingReworkCoefficient));
        stagingReworkField.setDisable(true);
        setStagingReworkButton.setText("Change");

        photoshopField.setText(String.valueOf(ValuesData.photoshopCoefficient));
        photoshopField.setDisable(true);
        setPhotoshopButton.setText("Change");

        photoshopReworkField.setText(String.valueOf(ValuesData.photoshopReworkCoefficient));
        photoshopReworkField.setDisable(true);
        setPhotoshopReworkButton.setText("Change");

        limitSet = true;
        wallsSet = true;
        wallReworksSet = true;
        stagingSet = true;
        stagingReworkSet = true;
        photoshopSet = true;
        photoshopReworkSet = true;
    }

    @FXML
    public void changeOrSetLimit(){

        if (limitSet){

            limitField.setDisable(false);

            setLimitButton.setText("Set");

            limitSet = false;

        }
        else {
            try {
                MainWindowController.countLimit = Double.parseDouble(limitField.getText());

                Document filter = new Document("Name","Settings");

                Document doc = (Document) MyMongoDatabase.collectionUser.find(filter).first();

                if(doc != null){
                    Document update = new Document("$set",new Document()
                            .append("Limit",String.valueOf(MainWindowController.countLimit))
                    );

                    MyMongoDatabase.collectionUser.updateOne(filter,update);

                    limitField.setDisable(true);

                    setLimitButton.setText("Change");

                    limitSet = true;
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");
                    alert.setHeaderText("Error setting limit");
                    alert.setContentText("There has been an error setting the limit. Try again later.");

                    alert.showAndWait();
                }
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid field input");
                alert.setContentText("The value you entered is not a number.");

                alert.showAndWait();
            }
        }

    }

    @FXML
    public void changeOrSetWalls(){
        if (wallsSet){

            wallField.setDisable(false);

            setWallButton.setText("Set");

            wallsSet = false;

        }
        else {
            try {
                ValuesData.wallCoefficient = Double.parseDouble(wallField.getText());

                Document filter = new Document("Name","Settings");

                Document doc = (Document) MyMongoDatabase.collectionUser.find(filter).first();

                if(doc != null){
                    Document update = new Document("$set",new Document()
                            .append("Wall Coefficient",String.valueOf(ValuesData.wallCoefficient))
                    );

                    MyMongoDatabase.collectionUser.updateOne(filter,update);

                    wallField.setDisable(true);

                    setWallButton.setText("Change");

                    wallsSet = true;

                    new CountPaneController();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");
                    alert.setHeaderText("Error setting wall coefficient");
                    alert.setContentText("There has been an error setting wall coefficient. Try again later.");

                    alert.showAndWait();
                }
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid field input");
                alert.setContentText("The value you entered is not a number.");

                alert.showAndWait();
            }
        }
    }

    @FXML
    public void changeOrSetWallReworks(){
        if (wallReworksSet){

            wallReworkField.setDisable(false);

            setWallReworkButton.setText("Set");

            wallReworksSet = false;

        }
        else {
            try {
                ValuesData.wallReworkCoefficient = Double.parseDouble(wallReworkField.getText());

                Document filter = new Document("Name","Settings");

                Document doc = (Document) MyMongoDatabase.collectionUser.find(filter).first();

                if(doc != null){
                    Document update = new Document("$set",new Document()
                            .append("Wall Rework Coefficient",String.valueOf(ValuesData.wallReworkCoefficient))
                    );

                    MyMongoDatabase.collectionUser.updateOne(filter,update);

                    wallReworkField.setDisable(true);

                    setWallButton.setText("Change");

                    wallReworksSet = true;
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");
                    alert.setHeaderText("Error setting wall rework coefficient");
                    alert.setContentText("There has been an error setting wall rework coefficient. Try again later.");

                    alert.showAndWait();
                }
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid field input");
                alert.setContentText("The value you entered is not a number.");

                alert.showAndWait();
            }
        }
    }

    @FXML
    public void changeOrSetStagings(){
        if (stagingSet){

            stagingField.setDisable(false);

            setStagingButton.setText("Set");

            stagingSet = false;

        }
        else {
            try {
                ValuesData.stagingCoefficient = Double.parseDouble(stagingField.getText());

                Document filter = new Document("Name","Settings");

                Document doc = (Document) MyMongoDatabase.collectionUser.find(filter).first();

                if(doc != null){
                    Document update = new Document("$set",new Document()
                            .append("Staging Coefficient",String.valueOf(ValuesData.stagingCoefficient))
                    );

                    MyMongoDatabase.collectionUser.updateOne(filter,update);

                    stagingField.setDisable(true);

                    setStagingButton.setText("Change");

                    stagingSet = true;
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");
                    alert.setHeaderText("Error setting staging coefficient");
                    alert.setContentText("There has been an error setting staging coefficient. Try again later.");

                    alert.showAndWait();
                }
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid field input");
                alert.setContentText("The value you entered is not a number.");

                alert.showAndWait();
            }
        }
    }

    @FXML
    public void changeOrSetStagingReworks(){
        if (stagingReworkSet){

            stagingReworkField.setDisable(false);

            setStagingButton.setText("Set");

            stagingReworkSet = false;

        }
        else {
            try {
                ValuesData.stagingReworkCoefficient = Double.parseDouble(stagingReworkField.getText());

                Document filter = new Document("Name","Settings");

                Document doc = (Document) MyMongoDatabase.collectionUser.find(filter).first();

                if(doc != null){
                    Document update = new Document("$set",new Document()
                            .append("Staging Rework Coefficient",String.valueOf(ValuesData.stagingReworkCoefficient))
                    );

                    MyMongoDatabase.collectionUser.updateOne(filter,update);

                    stagingReworkField.setDisable(true);

                    setStagingReworkButton.setText("Change");

                    stagingReworkSet = true;
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");
                    alert.setHeaderText("Error setting staging rework coefficient");
                    alert.setContentText("There has been an error setting staging rework coefficient. Try again later.");

                    alert.showAndWait();
                }
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid field input");
                alert.setContentText("The value you entered is not a number.");

                alert.showAndWait();
            }
        }
    }

    @FXML
    public void changeOrSetPhotoshops(){
        if (photoshopSet){

            photoshopField.setDisable(false);

            setPhotoshopButton.setText("Set");

            photoshopSet = false;

        }
        else {
            try {
                ValuesData.photoshopCoefficient = Double.parseDouble(photoshopField.getText());

                Document filter = new Document("Name","Settings");

                Document doc = (Document) MyMongoDatabase.collectionUser.find(filter).first();

                if(doc != null){
                    Document update = new Document("$set",new Document()
                            .append("Photoshop Coefficient",String.valueOf(ValuesData.photoshopCoefficient))
                    );

                    MyMongoDatabase.collectionUser.updateOne(filter,update);

                    photoshopField.setDisable(true);

                    setPhotoshopButton.setText("Change");

                    photoshopSet = true;
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");
                    alert.setHeaderText("Error setting photoshop coefficient");
                    alert.setContentText("There has been an error setting photoshop coefficient. Try again later.");

                    alert.showAndWait();
                }
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid field input");
                alert.setContentText("The value you entered is not a number.");

                alert.showAndWait();
            }
        }
    }

    @FXML
    public void changeOrSetPhotoshopReworks(){
        if (photoshopReworkSet){

            photoshopReworkField.setDisable(false);

            setPhotoshopReworkButton.setText("Set");

            photoshopReworkSet = false;

        }
        else {
            try {
                ValuesData.photoshopReworkCoefficient = Double.parseDouble(photoshopReworkField.getText());

                Document filter = new Document("Name","Settings");

                Document doc = (Document) MyMongoDatabase.collectionUser.find(filter).first();

                if(doc != null){
                    Document update = new Document("$set",new Document()
                            .append("Photoshop Rework Coefficient",String.valueOf(ValuesData.photoshopReworkCoefficient))
                    );

                    MyMongoDatabase.collectionUser.updateOne(filter,update);

                    photoshopReworkField.setDisable(true);

                    setPhotoshopButton.setText("Change");

                    photoshopReworkSet = true;
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Error");
                    alert.setHeaderText("Error setting photoshop rework coefficient");
                    alert.setContentText("There has been an error setting photoshop rework coefficient. Try again later.");

                    alert.showAndWait();
                }
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid field input");
                alert.setContentText("The value you entered is not a number.");

                alert.showAndWait();
            }
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @FXML
    public void changeEmailAddress(){

        if (!currentEmailField.getText().isEmpty()){
            Document doc = (Document) MyMongoDatabase.collectionUser.find(new Document("email",currentEmailField.getText())).first();

            if (doc!=null){

                if (isValidEmailAddress(newEmailField.getText())){

                    LoginScreenController.userEmailAddress = newEmailField.getText();

                    Document filter = new Document("email",currentEmailField.getText());

                    Document update = new Document("$set",new Document()
                            .append("email",LoginScreenController.userEmailAddress)
                    );

                    MyMongoDatabase.collectionUser.updateOne(filter,update);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Changed Address Successfully");
                    alert.setHeaderText("Your email address has been changed");
                    alert.setContentText("Email address for your profile has been changed to \"" + LoginScreenController.userEmailAddress + "\" successfully.");

                    alert.showAndWait();

                    Scene scene = MainWindow.newStage.getScene();

                    Label emailLabel = (Label) scene.lookup("#userEmailLabel");
                    emailLabel.setText(LoginScreenController.userEmailAddress);

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Invalid Email Address");
                    alert.setHeaderText("Text entered not an address");
                    alert.setContentText("The text you entered is not an email address. Enter a valid email address.");

                    alert.showAndWait();
                }

            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Email Address");
                alert.setHeaderText("Entered email address incorrect");
                alert.setContentText("The email address you entered does not match the one in our database.");

                alert.showAndWait();
            }
        }

    }

    public boolean isValidPassword(String password){
        String numRegex   = ".*[0-9].*";
        String alphaRegex = ".*[a-zA-Z].*";

        if (password.length()>=4){

            if (password.matches(alphaRegex)){

                if (password.matches(numRegex)){

                    return true;

                }
                else {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("No Numbers");
                    alert.setHeaderText("No numbers in new password");
                    alert.setContentText("Password you entered has to contain at least 1 number.");

                    alert.showAndWait();

                    return false;
                }

            }
            else {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("No Characters");
                alert.setHeaderText("No letters in new password");
                alert.setContentText("Password you entered has to contain at least 1 letter.");

                alert.showAndWait();

                return false;
            }

        }
        else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Password Too Short");
            alert.setHeaderText("Your password is too short");
            alert.setContentText("Password you entered has to be at least 4 characters long.");

            alert.showAndWait();

            return false;
        }
    }

    @FXML
    public void changePassword(){

        if (!currentPasswordField.getText().isEmpty()){

            Document doc = (Document) MyMongoDatabase.collectionUser.find(new Document("password",currentPasswordField.getText())).first();

            if (doc!=null) {
                if (isValidPassword(newPasswordField.getText())){
                    if (newPasswordField.getText().equals(confirmPasswordField.getText())){
                        Document filter = new Document("password",currentPasswordField.getText());

                        Document update = new Document("$set",new Document()
                                .append("password",newPasswordField.getText())
                        );

                        MyMongoDatabase.collectionUser.updateOne(filter,update);


                        Alert alert = new Alert(Alert.AlertType.INFORMATION);

                        alert.setTitle("Changed Password Successfully");
                        alert.setHeaderText("Your password has been changed");
                        alert.setContentText("Password for your profile has been changed successfully.");

                        alert.showAndWait();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);

                        alert.setTitle("Passwords Do Not Match");
                        alert.setHeaderText("The two password do not match");
                        alert.setContentText("New password and confirmation password do not match.");

                        alert.showAndWait();
                    }
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Password Incorrect");
                alert.setHeaderText("Current password is incorrect");
                alert.setContentText("Current password you entered does not match the one in our database.");

                alert.showAndWait();
            }

        }

    }

}
