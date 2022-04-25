package com.example.catalog;

import com.example.catalog.exception.item.ItemNotExistException;
import com.example.catalog.exception.type.TypeExistException;
import com.example.catalog.exception.type.TypeNotExistException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatalogController {
    private final TypeContainer typeContainer = new TypeContainer();
    private final ItemContainer itemContainer = new ItemContainer();
    @FXML
    private TreeView<String> view;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button exitButton;

    @FXML
    TableView<List<String>> table;

    @FXML
    private void initialize() {
        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(true);
        view.setRoot(root);
        view.getSelectionModel().select(root);
        List<String> bookFieldTypes = new ArrayList<>();
        bookFieldTypes.add("title");
        bookFieldTypes.add("authors");
        bookFieldTypes.add("edition");
        bookFieldTypes.add("purchase date");
        bookFieldTypes.add("completed date");

        List<String> cdFieldTypes = new ArrayList<>();
        cdFieldTypes.add("title");
        cdFieldTypes.add("colour");
        cdFieldTypes.add("speed");

        try {
            typeContainer.add(new Type("book", bookFieldTypes), view);
            typeContainer.add(new Type("cd", cdFieldTypes), view);
        } catch (TypeExistException e) {
            e.printStackTrace();
        }
        addButton.setOnAction((actionEvent -> onAdd()));
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());
        helpButton.setOnAction((actionEvent -> onHelp()));
        exitButton.setOnAction(actionEvent -> onExit());
        view.setOnMouseClicked(mouseEvent -> onSelect());
    }

    public void onAdd() {
        TreeItem<String> treeItem = view.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (treeItem instanceof Item) {
            alert.setHeaderText(Localisation.SELECT + Localisation.ROOT + " or " + Localisation.TYPE);
            alert.show();
        } else if (treeItem instanceof Type type) {
            onAddItem(type, alert);
        } else {
            onAddType(alert);
        }
    }

    public void onAddItem(Type type, Alert alert) {
        Dialog<Item> dialog = new Dialog<>();
        dialog.setTitle(Localisation.ADD + " " + Localisation.ITEM);
        DialogPane pane = new DialogPane();
        pane.getButtonTypes().add(ButtonType.OK);
        dialog.setDialogPane(pane);
        GridPane content = new GridPane();
        pane.setContent(content);
        TextField name = new TextField(type.getName());
        content.add(new Label("name"), 0, 0);
        content.add(name, 1, 0);
        List<TextField> textFields = new ArrayList<>();

        for (int i = 0; i < type.getFieldTypes().size(); i++) {
            textFields.add(new TextField(type.getFieldTypes().get(i)));
        }

        for (int i = 0; i < textFields.size(); i++) {
            content.add(new Label(type.getFieldTypes().get(i)), 0, i + 2);
            content.add(textFields.get(i), 1, i + 2);
        }
        dialog.setResultConverter(buttonType -> {
            List<String> fieldValues = new ArrayList<>();

            for (TextField f : textFields) {
                fieldValues.add(f.getText());
            }
            return new Item(name.getText(), type, fieldValues);
        });
        Optional<Item> result = dialog.showAndWait();

        if (result.isPresent()) {
            Item item = result.get();

            if (item.getName().trim().isEmpty()) {
                alert.setHeaderText(Localisation.EMPTY_NAME);
                alert.show();
            } else {
                itemContainer.add(item);
            }
        }
    }

    private void onAddType(Alert alert) {
        Dialog<Type> dialog = new Dialog<>();
        dialog.setTitle(Localisation.ADD + " " + Localisation.TYPE);
        DialogPane pane = new DialogPane();
        pane.getButtonTypes().add(ButtonType.OK);
        dialog.setDialogPane(pane);
        GridPane content = new GridPane();
        pane.setContent(content);
        TextField name = new TextField();
        TextField fields = new TextField();
        content.add(new Label("name"), 0, 0);
        content.add(name, 1, 0);
        content.add(new Label("field types"), 0, 1);
        content.add(fields, 1, 1);
        dialog.setResultConverter(buttonType -> new Type(name.getText(), List.of(fields.getText().split(","))));
        dialog.setDialogPane(pane);
        Optional<Type> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                Type type = result.get();

                if (type.getName().trim().isEmpty()) {
                    alert.setHeaderText(Localisation.EMPTY_NAME);
                    alert.show();
                } else {
                    typeContainer.add(type, view);
                }
            } catch (TypeExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        }
    }

    private void onEdit() {
        TreeItem<String> treeItem = view.getSelectionModel().getSelectedItem();
        Dialog<Void> dialog = new Dialog<>();
        DialogPane pane = new DialogPane();
        pane.getButtonTypes().addAll(ButtonType.OK);
        dialog.setDialogPane(pane);
        GridPane content = new GridPane();
        pane.setContent(content);
        content.add(new Label("name"), 0, 0);
        TextField name = new TextField();
        content.add(name, 1, 0);
        List<TextField> textFields = new ArrayList<>();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (treeItem instanceof Type type) {
            dialog.setTitle("edit type");

            name.setText(type.getName());

            for (int i = 0; i < type.getFieldTypes().size(); i++) {
                textFields.add(new TextField(type.getFieldTypes().get(i)));
            }

            for (int i = 0; i < textFields.size(); i++) {
                content.add(new Label("field type " + (i + 1)), 0, i + 2);
                content.add(textFields.get(i), 1, i + 2);
            }
            dialog.showAndWait();

            if (name.getText().trim().isEmpty()) {
                alert.setHeaderText(Localisation.EMPTY_NAME);
                alert.show();
            } else {
                type.setName(name.getText());
                List<String> fieldTypes = new ArrayList<>();

                for (TextField f : textFields) {
                    fieldTypes.add(f.getText());
                }
                type.setFieldTypes(fieldTypes);
            }
        } else if (treeItem instanceof Item item) {
            dialog.setTitle("edit item");
            name.setText(item.getName());

            for (int i = 0; i < item.getFieldValues().size(); i++) {
                textFields.add(new TextField(item.getFieldValues().get(i)));
            }

            for (int i = 0; i < textFields.size(); i++) {
                content.add(new Label(item.getType().getFieldTypes().get(i)), 0, i + 2);
                content.add(textFields.get(i), 1, i + 2);
            }
            dialog.showAndWait();

            if (name.getText().trim().isEmpty()) {
                alert.setHeaderText(Localisation.EMPTY_NAME);
                alert.show();
            } else {
                item.setName(name.getText());
                List<String> fieldValues = new ArrayList<>();

                for (TextField f : textFields) {
                    fieldValues.add(f.getText());
                }
                item.setFieldValues(fieldValues);
            }
        } else {
            alert.setHeaderText(Localisation.SELECTED_ROOT);
            alert.show();
        }
    }

    private void onDelete() {
        TreeItem<String> treeItem = view.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (treeItem instanceof Type type) {
            try {
                typeContainer.remove(type, view, itemContainer);
            } catch (TypeNotExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        } else if (treeItem instanceof Item item) {
            try {
                itemContainer.remove(item);
            } catch (ItemNotExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        } else {
            alert.setHeaderText(Localisation.SELECTED_ROOT);
            alert.show();
        }
    }

    private void onHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Localisation.HELP);
        alert.setHeaderText("lorem ipsum dolor sit amet");
        alert.show();
    }

    private void onExit() {
        Platform.exit();
    }

    private void onSelect() {
        table.getColumns().clear();
        table.getItems().clear();

        TreeItem<String> treeItem = view.getSelectionModel().getSelectedItem();

        if (treeItem instanceof Type type) {
            for (String s : type.getFieldTypes()) {
                table.getColumns().add(new TableColumn<>(s));
            }
        } else if (treeItem instanceof Item item) {
            for (int i = 0; i < item.getType().getFieldTypes().size(); i++) {
                TableColumn<List<String>, String> column = new TableColumn<>(item.getType().getFieldTypes().get(i));
                int finalI = i;
                column.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().get(finalI)));
                table.getColumns().add(column);
            }
            List<String> fieldValues = FXCollections.observableArrayList();

            for (int i = 0; i < item.getFieldValues().size(); i++) {
                fieldValues.add(i, item.getFieldValues().get(i));
            }
            table.getItems().add(fieldValues);
        }
    }
}