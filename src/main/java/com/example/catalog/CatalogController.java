package com.example.catalog;

import com.example.catalog.exception.type.TypeExistException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;

public class CatalogController {

    /**
     * is constructed once
     */
    private final Types types = new Types();

    @FXML
    private TreeView<String> view;

    @FXML
    private Button addTypeButton;

    @FXML Button exitButton;

    @FXML
    private void initialize() {
        view.setRoot(new TreeItem<>()); // root contains type nodes
        view.getRoot().setExpanded(true); // children are showed by default
        // event bindings
        addTypeButton.setOnAction((actionEvent -> onAddType()));
        exitButton.setOnAction(actionEvent -> onExit());
    }

    /**
     * is called when add type button is clicked
     */
    private void onAddType() {
        Dialog<Type> dialog = new Dialog<>();
        dialog.setTitle("add type");
        DialogPane pane = new DialogPane();
        GridPane content = new GridPane();
        TextField name = new TextField();
        TextField fields = new TextField();
        content.add(new Label("name"), 0, 0); // x = 0, y = 0
        content.add(name, 1, 0);
        content.add(new Label("fields"), 0, 1);
        content.add(fields, 1, 1);
        pane.setContent(content);
        pane.getButtonTypes().add(ButtonType.OK);
        dialog.setResultConverter(buttonType -> new Type(name.getText(), List.of(fields.getText().trim().split(",")))); // text is trimmed before being split by commas
        dialog.setDialogPane(pane);
        Optional<Type> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                types.add(result.get());
                refresh();
            } catch (TypeExistException e) { // if type exists
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }

    /**
     * is called when exit button is clicked
     */
    private void onExit() {
        Platform.exit();
    }

    /**
     * refreshes the view
     */
    private void refresh() {
        view.getRoot().getChildren().clear();

        for (Type type : types.getAll()) {
            view.getRoot().getChildren().add(type.getNode());
        }
    }
}
