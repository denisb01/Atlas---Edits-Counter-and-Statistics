module com.example.atlas {
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.mongodb.driver.core;

    opens com.example.atlas to javafx.fxml;
    exports com.example.atlas;


}