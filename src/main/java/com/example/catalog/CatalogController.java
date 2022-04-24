package com.example.catalog;

import com.example.catalog.exception.item.ItemExistException;
import com.example.catalog.exception.item.ItemNotExistException;
import com.example.catalog.exception.type.TypeExistException;
import com.example.catalog.exception.type.TypeNotExistException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatalogController {
    /**
     * containers are constructed once
     */
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
    private void initialize() {
        view.setRoot(new TreeItem<>(Type.PREFIX)); // the root contains the type nodes
        view.getRoot().setExpanded(true); // the children are showed by default
        // read the types.txt and items.txt files
        // default types
        List<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("title");
        fieldTypes.add("author");
        fieldTypes.add("date");

        try {
            typeContainer.add(new Type(Type.PREFIX + "book", fieldTypes), view);
            typeContainer.add(new Type(Type.PREFIX + "cd", fieldTypes), view);
        } catch (TypeExistException e) {
            e.printStackTrace();
        }
        // event bindings
        addButton.setOnAction((actionEvent -> onAdd()));
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());
        helpButton.setOnAction((actionEvent -> onHelp()));
        exitButton.setOnAction(actionEvent -> onExit());
    }

    public void onAdd() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        TreeItem<String> selectedItem = view.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            alert.setHeaderText("""
            1. select the root to add a type
            2. select a type to add an item
            """);
            alert.show();
        } else if (selectedItem.getValue().equals(Type.PREFIX)) {
            onAddType(alert);
        } else if (selectedItem.getValue().startsWith(Type.PREFIX)) {
            onAddItem(selectedItem, alert);
        } else {
            alert.setHeaderText("nothing can be added to an item, however it can be edited");
            alert.show();
        }
    }

    public void onAddItem(TreeItem<String> selectedItem, Alert alert) {
        Dialog<Item> dialog = new Dialog<>();
        dialog.setTitle("add item");
        DialogPane pane = new DialogPane();
        pane.getButtonTypes().add(ButtonType.OK);
        dialog.setDialogPane(pane);
        GridPane content = new GridPane();
        pane.setContent(content);
        TextField name = new TextField();
        content.add(new Label("name"), 0, 0);
        content.add(name, 1, 0);
        List<TextField> textFields = new ArrayList<>();

        try {
            Type type = typeContainer.get(selectedItem.getValue());

            for (int i = 0; i < type.getFieldTypes().size(); i++) {
                textFields.add(new TextField(type.getFieldTypes().get(i)));
            }

            for (int i = 0; i < textFields.size(); i++) {
                content.add(new Label(type.getFieldTypes().get(i)), 0, i + 2);
                content.add(textFields.get(i), 1, i + 2);
            }
            List<String> fieldValues = new ArrayList<>();

            for (TextField f : textFields) {
                fieldValues.add(f.getText());
            }
            dialog.setResultConverter(buttonType -> {
                try {
                    return new Item(name.getText(), typeContainer.get(selectedItem.getValue()), fieldValues);
                } catch (TypeNotExistException e) {
                    alert.setHeaderText(e.getMessage());
                    alert.show();
                    return null;
                }
            });
            Optional<Item> result = dialog.showAndWait();

            if (result.isPresent()) {
                try {
                    Item item = result.get();
                    itemContainer.add(item);
                } catch (ItemExistException e) {
                    alert.setHeaderText(e.getMessage());
                    alert.show();
                }
            }
        } catch (TypeNotExistException e) {
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    /**
     * is called when add type button is clicked
     */
    private void onAddType(Alert alert) {
        Dialog<Type> dialog = new Dialog<>();
        dialog.setTitle("add type");
        DialogPane pane = new DialogPane();
        pane.getButtonTypes().add(ButtonType.OK);
        dialog.setDialogPane(pane);
        GridPane content = new GridPane();
        pane.setContent(content);
        TextField name = new TextField(Type.PREFIX);
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

                if (type.getName().equals(Type.PREFIX)) {
                    alert.setHeaderText("the type can not have the root's name");
                    alert.show();
                } else if (type.getName().startsWith(Type.PREFIX)) {
                    typeContainer.add(type, view);
                } else {
                    alert.setHeaderText("the type name should start with \"" + Type.PREFIX + "\"");
                    alert.show();
                }
            } catch (TypeExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        }
    }

    private void onEdit() {
        String value = view.getSelectionModel().getSelectedItem().getValue();

        if (value.equals("/")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("the root can not be edited");
            alert.show();
            return;
        }
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

        if (value.startsWith(Type.PREFIX)) { // a type is selected
            dialog.setTitle("edit type");

            try {
                Type type = typeContainer.get(value);
                name.setText(type.getName());

                for (int i = 0; i < type.getFieldTypes().size(); i++) {
                    textFields.add(new TextField(type.getFieldTypes().get(i)));
                }

                for (int i = 0; i < textFields.size(); i++) {
                    content.add(new Label("field type " + (i + 1)), 0, i + 2);
                    content.add(textFields.get(i), 1, i + 2);
                }
                dialog.showAndWait();

                if (name.getText().equals(Type.PREFIX)) {
                    alert.setHeaderText("the type can not have the root's name");
                    alert.show();
                } else if (name.getText().startsWith("/")) {
                    type.setName(name.getText());
                    List<String> fieldTypes = new ArrayList<>();

                    for (TextField f : textFields) {
                        fieldTypes.add(f.getText());
                    }
                    type.setFieldTypes(fieldTypes);
                } else {
                    alert.setHeaderText("the type name should start with \"" + Type.PREFIX + "\"");
                    alert.show();
                }
            } catch (TypeNotExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        } else { // an item is selected
            dialog.setTitle("edit item");

            try {
                Item item = itemContainer.get(value);
                name.setText(item.getName());

                for (int i = 0; i < item.getFieldValues().size(); i++) {
                    textFields.add(new TextField(item.getFieldValues().get(i)));
                }

                for (int i = 0; i < textFields.size(); i++) {
                    content.add(new Label(item.getType().getFieldTypes().get(i)), 0, i + 2);
                    content.add(textFields.get(i), 1, i + 2);
                }
                List<String> fieldValues = new ArrayList<>();

                for (TextField f : textFields) {
                    fieldValues.add(f.getText());
                }
                dialog.showAndWait();
                item.setName(name.getText());
                item.setFieldValues(fieldValues);
            } catch (ItemNotExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        }
    }

    private void onDelete() {
        String value = view.getSelectionModel().getSelectedItem().getValue();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (value.equals(Type.PREFIX)) {
            alert.setHeaderText("the root can not be deleted");
            alert.show();
            return;
        }

        if (value.startsWith("/")) { // a type is selected
            try {
                typeContainer.remove(typeContainer.get(value), view, itemContainer);
            } catch (TypeNotExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        } else { // an item is selected
            try {
                Item item = itemContainer.get(value);
                itemContainer.remove(item);
            } catch (ItemNotExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        }
    }

    private void onHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("help");
        alert.setHeaderText("lorem ipsum dolor sit amet");
        alert.show();
    }

    /**
     * is called when exit button is clicked
     */
    private void onExit() {
        Platform.exit();
    }
}