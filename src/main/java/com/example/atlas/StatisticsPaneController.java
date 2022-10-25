package com.example.atlas;

import com.mongodb.client.FindIterable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class StatisticsPaneController implements Initializable {

    private Pane pane;

    private Button countButton;
    private Button statisticsButton;
    private Button autoClickerButton;
    private Button settingsButton;

    private LocalDate pickedDate;

    private int selectedMonth;
    private int selectedYear;

    private String status = "Unknown";

    private double percentage = 0;

    public static ValuesData pickedDateData = new ValuesData();
    private ValuesData monthlyData = new ValuesData();

    private XYChart.Series series;

    private String name;

    @FXML
    private Label dateLabel;
    @FXML
    private TextField limitField;
    @FXML
    private Button setLimitButton;

    @FXML
    private DatePicker datePicker;
    @FXML
    private RadioButton monthlyRadio;
    @FXML
    private RadioButton weeklyRadio;
    @FXML
    private RadioButton dailyRadio;
    @FXML
    private RadioButton todayRadio;
    @FXML
    private RadioButton addDaysRadio;
    @FXML
    private RadioButton customRadio;
    @FXML
    private ComboBox importBox;

    @FXML
    private PieChart pieChart;
    @FXML
    private LineChart<Number,Number> lineChart;
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
    private Label percentageLabel;

    @FXML
    private TextField nameTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dateLabel.setText(LoginScreenController.date);

        Scene scene = MainWindow.newStage.getScene();

        countButton = (Button) scene.lookup("#countButton");
        statisticsButton = (Button) scene.lookup("#statisticsButton");
        autoClickerButton = (Button) scene.lookup("#autoClickerButton");
        settingsButton = (Button) scene.lookup("#settingsButton");

        statisticsButton.setDisable(true);

        pane = (Pane) scene.lookup("#panelStatus");

        scene.setOnKeyPressed(event -> {
            try {
                switch (event.getCode()) {
                    case DIGIT1:
                        switchToCountPane();
                        break;
                    case DIGIT3:
                        openAutoClicker();
                        break;
                    case DIGIT4:
                        switchToSettingsPane();
                        break;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        if(MainWindowController.statisticsLimitSet){
            limitField.setText(String.format("%.2f",MainWindowController.statisticsLimit));
            limitField.setDisable(true);
            setLimitButton.setText("Change");
            changeLabels(pickedDateData);
        }

        findAll();

        datePicker.setValue(LocalDate.now());

        dailyRadio.setSelected(true);

        changeLineChart();

        pickDate();

        limitField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                setLimit();
            }
        });

    }

    public void switchToCountPane() throws IOException {

        Pane newPane = FXMLLoader.load(getClass().getResource("count-pane.fxml"));

        pane.getChildren().add(newPane);

        countButton.setDisable(true);
        statisticsButton.setDisable(false);
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

    @FXML
    public void setLimit(){
        if(!MainWindowController.statisticsLimitSet) {
            try {
                MainWindowController.statisticsLimit = Double.parseDouble(limitField.getText());
                MainWindowController.statisticsLimitSet = true;
                limitField.setDisable(true);
                setLimitButton.setText("Change");
                changeLabels(pickedDateData);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid Limit Input");
                alert.setContentText("The value you entered is not a number.");

                alert.showAndWait();
            }
        }
        else{
            MainWindowController.statisticsLimitSet = false;
            limitField.setDisable(false);
            setLimitButton.setText("Set");
            limitField.requestFocus();
            changeLabels(pickedDateData);
        }
    }

    public void changeLabels(ValuesData data){
        data.calculateAll();

        wallEditsLabel.setText(String.format("%.2f",data.getWallEditsDone()));
        wallReworkEditsLabel.setText(String.format("%.2f",data.getWallReworkEditsDone()));
        stageEditsLabel.setText(String.format("%.2f",data.getStagingEditsDone()));
        stageReworkEditsLabel.setText(String.format("%.2f",data.getStagingReworkEditsDone()));
        photoshopEditsLabel.setText(String.format("%.2f",data.getPhotoshopEditsDone()));
        photoshopReworkEditsLabel.setText(String.format("%.2f",data.getPhotoshopReworkEditsDone()));

        totalEditsLabel.setText(String.format("%.2f",data.getTotalEdits()));

        if(MainWindowController.statisticsLimitSet){
            if(data.getTotalEdits()<MainWindowController.statisticsLimit){
                percentage = (1-(data.getTotalEdits()/MainWindowController.statisticsLimit))*100;
                double editsOverUnder = MainWindowController.statisticsLimit-data.getTotalEdits();

                percentageLabel.setStyle("-fx-background-color:#FF3B28;" +
                                         "-fx-text-fill:#ffffff;" +
                                         "-fx-font-family:\"Comic Sans MS\";" +
                                         "-fx-font-size:13;" +
                                         "-fx-background-radius:10;");
                percentageLabel.setText(
                        "You are under limit.\n" +
                        "You are under limit by: " + String.format("%.2f",percentage) + "%.\n" +
                        "To be at limit you need: " + String.format("%.2f",editsOverUnder) + " edits.");

                status = "Under Limit";
            }
            else if(data.getTotalEdits()>MainWindowController.statisticsLimit){
                percentage = ((data.getTotalEdits()/MainWindowController.statisticsLimit)-1)*100;
                double editsOverUnder = data.getTotalEdits()-MainWindowController.statisticsLimit;

                percentageLabel.setStyle("-fx-background-color:#48EB12;"+
                                         "-fx-text-fill:#ffffff;" +
                                         "-fx-font-family:\"Comic Sans MS\";" +
                                         "-fx-font-size:13;" +
                                         "-fx-background-radius:10;");
                percentageLabel.setText(
                        "You are over limit.\n" +
                        "You are over limit by: " + String.format("%.2f",percentage) + "%.\n" +
                        "You are over limit by: " + String.format("%.2f",editsOverUnder) + " edits.");

                status = "Over Limit";
            }
            else{
                percentage = 0;

                percentageLabel.setStyle("-fx-background-color:#F0BE0C;"+
                                         "-fx-text-fill:#ffffff;" +
                                         "-fx-font-family:\"Comic Sans MS\";" +
                                         "-fx-font-size:13;" +
                                         "-fx-background-radius:10;");
                percentageLabel.setText(
                        "You are exactly at the limit.\n");

                status = "Exactly At Limit";
            }
        }
        else{
            percentageLabel.setStyle("-fx-background-color:#6490E8;"+
                                     "-fx-text-fill:#ffffff;" +
                                     "-fx-font-family:\"Comic Sans MS\";" +
                                     "-fx-font-size:13;" +
                                     "-fx-background-radius:10;");
            percentageLabel.setText("Set limit first.");

            status = "Unknown";

            percentage = 0;
        }

        changePieChart(data);
    }

    public void changePieChart(ValuesData data){
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Walls", data.getWallEditsDone()),
                        new PieChart.Data("Wall Reworks", data.getWallReworkEditsDone()),
                        new PieChart.Data("Stagings", data.getStagingEditsDone()),
                        new PieChart.Data("Staging Reworks", data.getStagingReworkEditsDone()),
                        new PieChart.Data("Photoshops", data.getPhotoshopEditsDone()),
                        new PieChart.Data("Photoshop Reworks",data.getPhotoshopReworkEditsDone()));

        pieChart.setData(pieChartData);

        pieChart.getData().forEach(pieData -> {
            String percentage = String.format("%.2f%%",(pieData.getPieValue()/pickedDateData.getTotalEdits())*100);
            Tooltip tooltip = new Tooltip(percentage);
            Tooltip.install(pieData.getNode(),tooltip);
        });
    }

    public void changeLineChart(){
        YearMonth yearMonth = YearMonth.from(datePicker.getValue());

        selectedMonth = yearMonth.getMonthValue();
        selectedYear = yearMonth.getYear();

        int daysInMonth = yearMonth.lengthOfMonth();

        series = new XYChart.Series();
        series.setName("Edits This Month");

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                String dayOfMonth;

                for (int i=1;i<=daysInMonth;i++){

                    if(i<10) {
                        if(yearMonth.getMonthValue()<10) {
                            dayOfMonth = yearMonth.getYear() + "-" + "0" + yearMonth.getMonthValue() + "-" + "0" + i;
                        }
                        else {
                            dayOfMonth = yearMonth.getYear() + "-" + yearMonth.getMonthValue() + "-" + "0" + i;
                        }
                    }
                    else {
                        if(yearMonth.getMonthValue()<10) {
                            dayOfMonth = yearMonth.getYear() + "-" + "0" + yearMonth.getMonthValue() + "-" + i;
                        }
                        else {
                            dayOfMonth = yearMonth.getYear() + "-" + yearMonth.getMonthValue() + "-" + i;
                        }
                    }

                    Document filter = new Document("Date", dayOfMonth);

                    Document doc = (Document) MyMongoDatabase.collectionData.find(filter).first();

                    int l = i;

                    if(doc != null) {
                        monthlyData.setWallsDone((int)doc.get("Walls"));
                        monthlyData.setWallsReworkDone((int) doc.get("Wall Reworks"));
                        monthlyData.setStagingsDone((int) doc.get("Staging"));
                        monthlyData.setStagingsReworkDone((int) doc.get("Staging Reworks"));
                        monthlyData.setPhotoshopsDone((int) doc.get("Photoshops"));
                        monthlyData.setPhotoshopsReworkDone((int) doc.get("Photoshop Reworks"));

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                series.getData().add(new XYChart.Data(String.valueOf(l),monthlyData.getTotalEdits()));

                                lineChart.getData().clear();
                                lineChart.getData().add(series);

                                for (Object entry : series.getData()) {
                                    Tooltip t = new Tooltip(((XYChart.Data<String,Number>)entry).getYValue().toString());
                                    Tooltip.install(((XYChart.Data<String,Number>)entry).getNode(), t);
                                }
                            }
                        });
                        Thread.sleep(200);
                    }
                    else{
                        monthlyData.reset();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                series.getData().add(new XYChart.Data(String.valueOf(l),monthlyData.getTotalEdits()));

                                lineChart.getData().clear();
                                lineChart.getData().add(series);

                                for (Object entry : series.getData()) {
                                    Tooltip t = new Tooltip(((XYChart.Data<String,Number>)entry).getYValue().toString());
                                    Tooltip.install(((XYChart.Data<String,Number>)entry).getNode(), t);
                                }
                            }
                        });
                        Thread.sleep(200);
                    }
                }
                return null;
            }

        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        String month = String.valueOf(yearMonth.getMonth());

        month = month.toLowerCase(Locale.ROOT);
        month = month.substring(0, 1).toUpperCase() + month.substring(1);

        lineChart.setTitle("Monthly Graph - "+month);

    }

    public void getWeeklyEdits() {
        LocalDate dt = datePicker.getValue();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(sdf.parse(dt.toString()));

            Document filter = new Document("Date", dt.toString());

            Document doc = (Document) MyMongoDatabase.collectionData.find(filter).first();

            if(doc != null) {
                pickedDateData.setWallsDone((int) doc.get("Walls"));
                pickedDateData.setWallsReworkDone((int) doc.get("Wall Reworks"));
                pickedDateData.setStagingsDone((int) doc.get("Staging"));
                pickedDateData.setStagingsReworkDone((int) doc.get("Staging Reworks"));
                pickedDateData.setPhotoshopsDone((int) doc.get("Photoshops"));
                pickedDateData.setPhotoshopsReworkDone((int) doc.get("Photoshop Reworks"));
            }
            else{
                pickedDateData.reset();
            }
            for (int i=0;i<6;i++){
                c.add(Calendar.DATE, 1);
                dt = LocalDate.parse(sdf.format(c.getTime()));

                filter = new Document("Date",dt.toString());
                doc = (Document) MyMongoDatabase.collectionData.find(filter).first();

                if(doc != null) {
                    pickedDateData.setWallsDone(pickedDateData.getWallsDone()+doc.getInteger("Walls"));
                    pickedDateData.setWallsReworkDone(pickedDateData.getWallsReworkDone()+doc.getInteger("Wall Reworks"));
                    pickedDateData.setStagingsDone(pickedDateData.getStagingsDone()+doc.getInteger("Staging"));
                    pickedDateData.setStagingsReworkDone(pickedDateData.getStagingsReworkDone()+doc.getInteger("Staging Reworks"));
                    pickedDateData.setPhotoshopsDone(pickedDateData.getPhotoshopsDone()+doc.getInteger("Photoshops"));
                    pickedDateData.setPhotoshopsReworkDone(pickedDateData.getPhotoshopsReworkDone()+doc.getInteger("Photoshop Reworks"));
                }
                else{

                }

            }
        }
        catch (ParseException e){
            e.printStackTrace();
        }

    }

    public void getMonthlyEdits(){

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                YearMonth yearMonth = YearMonth.from(datePicker.getValue());

                int daysInMonth = yearMonth.lengthOfMonth();

                String dayOfMonth;

                pickedDateData.reset();

                for (int i = 1; i <= daysInMonth; i++) {

                    if (i < 10) {
                        if (yearMonth.getMonthValue() < 10) {
                            dayOfMonth = yearMonth.getYear() + "-" + "0" + yearMonth.getMonthValue() + "-" + "0" + i;
                        } else {
                            dayOfMonth = yearMonth.getYear() + "-" + yearMonth.getMonthValue() + "-" + "0" + i;
                        }
                    } else {
                        if (yearMonth.getMonthValue() < 10) {
                            dayOfMonth = yearMonth.getYear() + "-" + "0" + yearMonth.getMonthValue() + "-" + i;
                        } else {
                            dayOfMonth = yearMonth.getYear() + "-" + yearMonth.getMonthValue() + "-" + i;
                        }
                    }

                    Document filter = new Document("Date", dayOfMonth);

                    Document doc = (Document) MyMongoDatabase.collectionData.find(filter).first();

                    if (doc != null) {
                        pickedDateData.setWallsDone(pickedDateData.getWallsDone() + doc.getInteger("Walls"));
                        pickedDateData.setWallsReworkDone(pickedDateData.getWallsReworkDone() + doc.getInteger("Wall Reworks"));
                        pickedDateData.setStagingsDone(pickedDateData.getStagingsDone() + doc.getInteger("Staging"));
                        pickedDateData.setStagingsReworkDone(pickedDateData.getStagingsReworkDone() + doc.getInteger("Staging Reworks"));
                        pickedDateData.setPhotoshopsDone(pickedDateData.getPhotoshopsDone() + doc.getInteger("Photoshops"));
                        pickedDateData.setPhotoshopsReworkDone(pickedDateData.getPhotoshopsReworkDone() + doc.getInteger("Photoshop Reworks"));
                    } else {

                    }
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        changeLabels(pickedDateData);
                    }
                });

                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    @FXML
    public void pickDate(){
        if(todayRadio.isSelected()) {

            pickedDate = LocalDate.now();

            String date = pickedDate.toString();

            Document filter = new Document("Date", date);

            Document doc = (Document) MyMongoDatabase.collectionData.find(filter).first();

            if(doc != null) {
                pickedDateData.setWallsDone((Integer) doc.get("Walls"));
                pickedDateData.setWallsReworkDone((Integer) doc.get("Wall Reworks"));
                pickedDateData.setStagingsDone((Integer) doc.get("Staging"));
                pickedDateData.setStagingsReworkDone((Integer) doc.get("Staging Reworks"));
                pickedDateData.setPhotoshopsDone((Integer) doc.get("Photoshops"));
                pickedDateData.setPhotoshopsReworkDone((Integer) doc.get("Photoshop Reworks"));

                changeLabels(pickedDateData);
            }
            else{
                pickedDateData.reset();

                changeLabels(pickedDateData);
            }
        }
        else if(dailyRadio.isSelected()){
            pickedDate = datePicker.getValue();

            String date = pickedDate.toString();

            Document filter = new Document("Date", date);

            Document doc = (Document) MyMongoDatabase.collectionData.find(filter).first();

            if(doc != null) {
                pickedDateData.setWallsDone((Integer) doc.get("Walls"));
                pickedDateData.setWallsReworkDone((Integer) doc.get("Wall Reworks"));
                pickedDateData.setStagingsDone((Integer) doc.get("Staging"));
                pickedDateData.setStagingsReworkDone((Integer) doc.get("Staging Reworks"));
                pickedDateData.setPhotoshopsDone((Integer) doc.get("Photoshops"));
                pickedDateData.setPhotoshopsReworkDone((Integer) doc.get("Photoshop Reworks"));

                changeLabels(pickedDateData);
            }
            else{
                pickedDateData.reset();

                changeLabels(pickedDateData);
            }
        }
        else if(addDaysRadio.isSelected()){
            pickedDate = datePicker.getValue();

            String date = pickedDate.toString();

            Document filter = new Document("Date", date);

            Document doc = (Document) MyMongoDatabase.collectionData.find(filter).first();

            if(doc != null) {
                pickedDateData.setWallsDone( pickedDateData.getWallsDone()+doc.getInteger("Walls"));
                pickedDateData.setWallsReworkDone(pickedDateData.getWallsReworkDone() + doc.getInteger("Wall Reworks"));
                pickedDateData.setStagingsDone(pickedDateData.getStagingsDone()+doc.getInteger("Staging"));
                pickedDateData.setStagingsReworkDone(pickedDateData.getStagingsReworkDone()+doc.getInteger("Staging Reworks"));
                pickedDateData.setPhotoshopsDone(pickedDateData.getPhotoshopsDone()+doc.getInteger("Photoshops"));
                pickedDateData.setPhotoshopsReworkDone(pickedDateData.getPhotoshopsReworkDone()+doc.getInteger("Photoshop Reworks"));

                changeLabels(pickedDateData);
            }
            else{
                pickedDateData.setWallsDone( pickedDateData.getWallsDone()+0);
                pickedDateData.setWallsReworkDone(pickedDateData.getWallsReworkDone()+0);
                pickedDateData.setStagingsDone(pickedDateData.getStagingsDone()+0);
                pickedDateData.setStagingsReworkDone(pickedDateData.getStagingsReworkDone()+0);
                pickedDateData.setPhotoshopsDone(pickedDateData.getPhotoshopsDone()+0);
                pickedDateData.setPhotoshopsReworkDone(pickedDateData.getPhotoshopsReworkDone()+0);

                changeLabels(pickedDateData);
            }
        }
        else if(weeklyRadio.isSelected()){
            getWeeklyEdits();

            changeLabels(pickedDateData);
        }
        else if(monthlyRadio.isSelected()){
            getMonthlyEdits();
        }
        else if(customRadio.isSelected()){

            Document doc = (Document) MyMongoDatabase.collectionSaves.find(new Document("Name",name)).first();

            if(doc!=null) {
                pickedDateData.setWallsDone(doc.getInteger("Walls"));
                pickedDateData.setWallsReworkDone(doc.getInteger("Wall Reworks"));
                pickedDateData.setStagingsDone(doc.getInteger("Staging"));
                pickedDateData.setStagingsReworkDone(doc.getInteger("Staging Reworks"));
                pickedDateData.setPhotoshopsDone(doc.getInteger("Photoshops"));
                pickedDateData.setPhotoshopsReworkDone(doc.getInteger("Photoshop Reworks"));

                changeLabels(pickedDateData);
            }
            else {
                pickedDateData.reset();

                changeLabels(pickedDateData);
            }
        }
    }

    public void changeGraphMonth(){
        YearMonth yearMonth = YearMonth.from(datePicker.getValue());

        boolean month = !(yearMonth.getMonthValue()==selectedMonth);
        boolean year = !(yearMonth.getYear()==selectedYear);



        if (month || year){
            series.getData().clear();
            lineChart.getData().clear();
            changeLineChart();
        }
    }

    @FXML
    public void saveCustom(){
        if(!nameTextField.getText().isEmpty()){

            Document doc = (Document) MyMongoDatabase.collectionSaves.find(new Document("Name",nameTextField.getText())).first();

            if(doc != null){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Invalid Name");
                alert.setHeaderText("You must change the name.");
                alert.setContentText("There already exists a file with this name.\nChange the name to save properly.");

                alert.showAndWait();
            }
            else {
                MyMongoDatabase.collectionSaves.insertOne(new Document()
                        .append("Name", nameTextField.getText())
                        .append("Status", status)
                        .append("Walls",pickedDateData.getWallsDone())
                        .append("Wall Reworks",pickedDateData.getWallsReworkDone())
                        .append("Staging",pickedDateData.getStagingsDone())
                        .append("Staging Reworks",pickedDateData.getStagingsReworkDone())
                        .append("Photoshops",pickedDateData.getPhotoshopsDone())
                        .append("Photoshop Reworks",pickedDateData.getPhotoshopsReworkDone())
                        .append("Percentage", String.format("%.2f", percentage) + "%"));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Successful");
                alert.setHeaderText("Saved Successfully.");
                alert.setContentText("Your file has been saved successfully.\nYou can access it under the name: "+nameTextField.getText()+".");

                alert.showAndWait();

                findAll();
            }
        }
    }

    public void findAll(){

        importBox.getItems().clear();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FindIterable<Document> iterable = MyMongoDatabase.collectionSaves.find();
                iterable.forEach(doc -> {
                    importBox.getItems().add(doc.get("Name"));
                });
            }
        });

    }

    @FXML
    public void select(){

        name = (String) importBox.getValue();

        pickDate();

    }

    @FXML
    public void delete(){
        Document doc = (Document) MyMongoDatabase.collectionSaves.find(new Document("Name",nameTextField.getText())).first();

        if (doc!=null){
            MyMongoDatabase.collectionSaves.deleteOne(doc);

            findAll();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Not found.");
            alert.setHeaderText("File cannot be deleted.");
            alert.setContentText("There is no file with the name:\""+nameTextField.getText()+"\" in database.\nEnter the correct name to delete.");

            alert.showAndWait();
        }
    }
}
