package com.example.abslouteloader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {


    @FXML
    public static  Button SIC;
    @FXML
    public static  Button SICXE;

    public void GoToSIC(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AbsoluteLoaderMem.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1030, 700);
        stage.setTitle("SIC Memory!");
        stage.setScene(scene);
        stage.show();
    }

    public void GoToSICXE(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LinkerLoader.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100, 820);
        stage.setTitle("SIC\\XE Memory!");
        //stage.setIconified(true);
        stage.setScene(scene);
        stage.show();
    }
}