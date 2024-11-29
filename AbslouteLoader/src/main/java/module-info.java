module com.example.abslouteloader {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.abslouteloader to javafx.fxml;
    exports com.example.abslouteloader;
}