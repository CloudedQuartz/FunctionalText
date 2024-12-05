package com.cloudedquartz.functionaltext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class FunctionalTextApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader homeLoader = new FXMLLoader(FunctionalTextApplication.class.getResource("/com/cloudedquartz/functionaltext/home-view.fxml"));

        if (homeLoader.getLocation() == null) {
            throw new IOException("FXML file not found");
        }

        Scene scene = new Scene(homeLoader.load());
        stage.setTitle("Functional Text");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}