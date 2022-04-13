package com.example.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CatalogApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("catalog-view.fxml"));
        VBox main = new VBox();
        Scene sahne = new Scene(root,600,400);




        stage.setTitle("Catalog App");
        stage.setScene(sahne);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
