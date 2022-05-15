package com.example.catalog;

import com.example.catalog.util.Localisation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class HelpController {

    @FXML
    private Button helpItemButton;

    @FXML
    private Button helpSearchButton;

    @FXML
    private Button helpTypeButton;


    @FXML
    private void initialize() {
        helpItemButton.setOnAction(actionEvent -> helpOnAdd());
        helpSearchButton.setOnAction(actionEvent -> helpOnSearch());
        helpTypeButton.setOnAction(actionEvent -> helpOnRemoveEdit());
    }

    @FXML
    void helpOnAdd() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Localisation.HELP_TITLE);
        alert.setHeaderText(Localisation.HELP_ADD_TEXT);
        alert.show();

    }

    @FXML
    void helpOnSearch() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Localisation.HELP_TITLE);
        alert.setHeaderText(Localisation.HELP_SEARCH_TEXT);
        alert.show();

    }

    @FXML
    void helpOnRemoveEdit() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Localisation.HELP_TITLE);
        alert.setHeaderText(Localisation.HELP_REMOVE_EDIT_TEXT);
        alert.show();
    }

    @FXML
    void helpOnSave() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Localisation.HELP_TITLE);
        alert.setHeaderText(Localisation.HELP_SAVE_TEXT);
        alert.show();
    }

    @FXML
    void helpOnExport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Localisation.HELP_TITLE);
        alert.setHeaderText(Localisation.HELP_EXPORT_TEXT);
        alert.show();
    }

    @FXML
    void helpOnPrint() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Localisation.HELP_TITLE);
        alert.setHeaderText(Localisation.HELP_PRINT_TEXT);
        alert.show();
    }
}