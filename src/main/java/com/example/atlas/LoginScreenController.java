package com.example.atlas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    public static ValuesData databaseData = new ValuesData();

    public static String userEmailAddress;

    public static String date = String.valueOf(java.time.LocalDate.now());

    @FXML
    private AnchorPane ap;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailTextField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                passwordTextField.requestFocus();
            }
        });

        passwordTextField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                try {
                    onLoginButtonClick();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    protected void onLoginButtonClick() throws IOException {

        Document user = (Document) MyMongoDatabase.collectionUser.find(new Document("email",emailTextField.getText())).first();

        if(user != null){

            if(user.get("password").equals(passwordTextField.getText())){

                System.out.println("Login Successful");

                userEmailAddress = user.get("email").toString();

                createOrReadDocument();

                startMainWindow();

            }
            else {
                System.out.println("Incorrect Password");

                Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect Password");
                alert.showAndWait();
            }
        }
        else{
            System.out.println("Incorrect Email Address");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect Email Address");
            alert.showAndWait();
        }

    }

    public void startMainWindow() throws IOException {

        Stage stage = (Stage) ap.getScene().getWindow();

        stage.close();

        new MainWindow();

    }

    public void createOrReadDocument(){
        Document doc = (Document) MyMongoDatabase.collectionData.find(new Document("Date",date)).first();

        if(doc != null){
            databaseData.setWallsDone(doc.getInteger("Walls"));
            databaseData.setWallsReworkDone(doc.getInteger("Wall Reworks"));
            databaseData.setStagingsDone(doc.getInteger("Staging"));
            databaseData.setStagingsReworkDone(doc.getInteger("Staging Reworks"));
            databaseData.setPhotoshopsDone(doc.getInteger("Photoshops"));
            databaseData.setPhotoshopsReworkDone(doc.getInteger("Photoshop Reworks"));
        }
        else{
            MyMongoDatabase.collectionData.insertOne(new Document()
                    .append("Date",date)
                    .append("Walls",databaseData.getWallsDone())
                    .append("Wall Reworks",databaseData.getWallsReworkDone())
                    .append("Staging",databaseData.getStagingsDone())
                    .append("Staging Reworks",databaseData.getStagingsReworkDone())
                    .append("Photoshops",databaseData.getPhotoshopsDone())
                    .append("Photoshop Reworks",databaseData.getPhotoshopsReworkDone())
            );
        }

        doc = (Document) MyMongoDatabase.collectionUser.find(new Document("Name","Settings")).first();

        if(doc != null){
            ValuesData.wallCoefficient = Double.parseDouble(doc.getString("Wall Coefficient"));
            ValuesData.wallReworkCoefficient = Double.parseDouble(doc.getString("Wall Rework Coefficient"));
            ValuesData.stagingCoefficient = Double.parseDouble(doc.getString("Staging Coefficient"));
            ValuesData.stagingReworkCoefficient = Double.parseDouble(doc.getString("Staging Rework Coefficient"));
            ValuesData.photoshopCoefficient = Double.parseDouble(doc.getString("Photoshop Coefficient"));
            ValuesData.photoshopReworkCoefficient = Double.parseDouble(doc.getString("Photoshop Rework Coefficient"));

            MainWindowController.countLimit = Double.parseDouble(doc.getString("Limit"));
        }

    }

}