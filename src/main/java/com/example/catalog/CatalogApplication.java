package com.example.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CatalogApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("catalog-view.fxml")));
        stage.setTitle("Catalog");
        stage.setScene(new Scene(parent, 900, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}