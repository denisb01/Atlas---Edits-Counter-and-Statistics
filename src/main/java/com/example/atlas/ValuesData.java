package com.example.atlas;

public class ValuesData {

    private int wallsDone = 0;
    private int wallsReworkDone = 0;
    private int stagingsDone = 0;
    private int stagingsReworkDone = 0;
    private int photoshopsDone = 0;
    private int photoshopsReworkDone = 0;

    private double wallEditsDone = 0;
    private double wallReworkEditsDone = 0;
    private double stagingEditsDone = 0;
    private double stagingReworkEditsDone = 0;
    private double photoshopEditsDone = 0;
    private double photoshopReworkEditsDone = 0;

    public static double wallCoefficient = 0.4;
    public static double wallReworkCoefficient = 0.2;
    public static double stagingCoefficient = 0.45;
    public static double stagingReworkCoefficient = 0.25;
    public static double photoshopCoefficient = 0.15;
    public static double photoshopReworkCoefficient = 0.15;

    private int total = 0;
    private double totalEdits = 0;

    public ValuesData() {

    }

    public void calculateTotal(){

        this.total = this.wallsDone + this.wallsReworkDone +this.stagingsDone + this.stagingsReworkDone + this.photoshopsDone + photoshopsReworkDone;

    }

    public void calculateWallEdits(){

        this.wallEditsDone = this.wallsDone*this.wallCoefficient;

    }

    public void calculateWallReworkEdits(){

        this.wallReworkEditsDone = this.wallsReworkDone*this.wallReworkCoefficient;

    }

    public void calculateStagingEdits(){

        this.stagingEditsDone = this.stagingsDone*this.stagingCoefficient;

    }

    public void calculateStagingReworkEdits(){

        this.stagingReworkEditsDone = this.stagingsReworkDone*this.stagingReworkCoefficient;

    }

    public void calculatePhotoshopEdits(){

        this.photoshopEditsDone = this.photoshopsDone*this.photoshopCoefficient;

    }

    public void calculatePhotoshopReworkEdits(){

        this.photoshopReworkEditsDone = this.photoshopsReworkDone*this.photoshopReworkCoefficient;

    }

    public void calculateAll(){

        calculateWallEdits();
        calculateWallReworkEdits();
        calculateStagingEdits();
        calculateStagingReworkEdits();
        calculatePhotoshopEdits();
        calculatePhotoshopReworkEdits();
        calculateTotal();
        calculateTotalEdits();

    }

    public void calculateTotalEdits(){

        this.totalEdits = this.wallEditsDone + this.wallReworkEditsDone + this.stagingEditsDone + stagingReworkEditsDone + photoshopEditsDone
                + photoshopReworkEditsDone;

    }

    public void reset(){

        this.wallsDone = 0;
        this.wallsReworkDone = 0;
        this.stagingsDone = 0;
        this.stagingsReworkDone = 0;
        this.photoshopsDone = 0;
        this.photoshopsReworkDone = 0;

        calculateAll();

    }

    public int getWallsDone() {
        return wallsDone;
    }

    public void setWallsDone(int wallsDone) {
        this.wallsDone = wallsDone;
        calculateAll();
    }

    public int getWallsReworkDone() {
        return wallsReworkDone;
    }

    public void setWallsReworkDone(int wallsReworkDone) {
        this.wallsReworkDone = wallsReworkDone;
        calculateAll();
    }

    public int getStagingsDone() {
        return stagingsDone;
    }

    public void setStagingsDone(int stagingsDone) {
        this.stagingsDone = stagingsDone;
        calculateAll();
    }

    public int getStagingsReworkDone() {
        return stagingsReworkDone;
    }

    public void setStagingsReworkDone(int stagingsReworkDone) {
        this.stagingsReworkDone = stagingsReworkDone;
        calculateAll();
    }

    public int getPhotoshopsDone() {
        return photoshopsDone;
    }

    public void setPhotoshopsDone(int photoshopsDone) {
        this.photoshopsDone = photoshopsDone;
        calculateAll();
    }

    public int getPhotoshopsReworkDone() {
        return photoshopsReworkDone;
    }

    public void setPhotoshopsReworkDone(int photoshopsReworkDone) {
        this.photoshopsReworkDone = photoshopsReworkDone;
        calculateAll();
    }

    public int getTotal() {
        return total;
    }

    public double getTotalEdits() {
        return totalEdits;
    }

    public double getWallEditsDone() {
        return wallEditsDone;
    }

    public double getWallReworkEditsDone() {
        return wallReworkEditsDone;
    }

    public double getStagingEditsDone() {
        return stagingEditsDone;
    }

    public double getStagingReworkEditsDone() {
        return stagingReworkEditsDone;
    }

    public double getPhotoshopEditsDone() {
        return photoshopEditsDone;
    }

    public double getPhotoshopReworkEditsDone() {
        return photoshopReworkEditsDone;
    }

}