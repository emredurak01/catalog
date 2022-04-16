package com.example.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class CatalogApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("catalog-view.fxml")));
        stage.setTitle("catalog");
        stage.setScene(new Scene(root,600,400));
        stage.show();
    }

    public static void main(String[] args) {
        //launch(args);

        Types types = new Types("Book");
        types.addType(types.getType());
        types.getType().addField("Name");
        types.getType().addField("Author");
        types.getType().removeField("Publisher");

        types.removeType(types.getType());
        types.removeType(types.getType());


    }
}