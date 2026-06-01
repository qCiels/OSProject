package org.OperatingSystems;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.show();

        stage.setTitle("CPU Scheduler");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}