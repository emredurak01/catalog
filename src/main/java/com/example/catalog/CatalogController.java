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
    public Button searchButton;

    @FXML
    private TreeView<String> view;

    @FXML
    private ComboBox<String> selectTypeBox;

    @FXML
    private Button addItemButton;

    @FXML
    private Button addTypeButton;

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
        view.setRoot(new TreeItem<>()); // root contains type nodes
        view.getRoot().setExpanded(true); // children are showed by default
        // read types and items from file
        // predefined types
        List<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("title");
        fieldTypes.add("author");
        fieldTypes.add("date");

        try {
            typeContainer.add(new Type("book", fieldTypes));
            typeContainer.add(new Type("cd", fieldTypes));
        } catch (TypeExistException e) {
            e.printStackTrace();
        }
        refresh();
        // event bindings
        addItemButton.setOnAction((actionEvent -> onAddItem()));
        addTypeButton.setOnAction((actionEvent -> onAddType()));
        editButton.setOnAction(actionEvent -> onEdit());
        deleteButton.setOnAction(actionEvent -> onDelete());
        helpButton.setOnAction((actionEvent -> onHelp()));
        exitButton.setOnAction(actionEvent -> onExit());
    }

    public void onAddItem() {
        Dialog<Item> dialog = new Dialog<>();
        dialog.setTitle("add item");
        DialogPane pane = new DialogPane();
        GridPane content = new GridPane();
        TextField name = new TextField();
        TextField fields = new TextField();
        content.add(new Label("name"), 0, 0); // x = 0, y = 0
        content.add(name, 1, 0);
        content.add(new Label("field values"), 0, 1);
        content.add(fields, 1, 1);
        pane.setContent(content);
        pane.getButtonTypes().add(ButtonType.OK);
        dialog.setResultConverter(buttonType -> {
            try {
                return new Item(name.getText(), typeContainer.get(selectTypeBox.getValue()), List.of(fields.getText().trim().split(",")));
            } catch (TypeNotExistException e) {
                e.printStackTrace();
                return null;
            }
        }); // text is trimmed before being split by commas
        dialog.setDialogPane(pane);
        Optional<Item> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                Item item = result.get();
                item.getType().getNode().getChildren().add(new TreeItem<>(item.getName()));
                itemContainer.add(item);

                refresh();
            } catch (ItemExistException e) { // if item exists
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
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
        content.add(new Label("field types"), 0, 1);
        content.add(fields, 1, 1);
        pane.setContent(content);
        pane.getButtonTypes().add(ButtonType.OK);
        dialog.setResultConverter(buttonType -> new Type(name.getText(), List.of(fields.getText().trim().split(",")))); // text is trimmed before being split by commas
        dialog.setDialogPane(pane);
        Optional<Type> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                typeContainer.add(result.get());
                refresh();
            } catch (TypeExistException e) { // if type exists
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }

    private void onEdit() { // in progress
        TreeItem<String> selectedItem = view.getSelectionModel().getSelectedItem();
        if (selectedItem.isLeaf()) { // if selectedItem is item
            Dialog<Item> dialog = new Dialog<>();
            DialogPane pane = new DialogPane();
            GridPane content = new GridPane();
            TextField name = new TextField();
            dialog.setTitle("edit item");
            Item item = null;
            try {
                item = itemContainer.get(selectedItem.getValue());
            } catch (ItemNotExistException e) {
                e.printStackTrace();
            }
            List<TextField> textFields = new ArrayList<>();

            for (int i = 0; i < item.getFieldValues().size(); i++) {
                textFields.add(new TextField());
            }
            content.add(new Label("name"), 0, 0); // x = 0, y = 0
            content.add(name, 1, 0);

            for (int i = 0; i < textFields.size(); i++) {
                content.add(new Label(item.getFieldValues().get(i)), 0, i + 2);
                content.add(textFields.get(i), 1, i + 2);
            }
            pane.setContent(content);
            pane.getButtonTypes().add(ButtonType.OK);
            try {
                List<String> fieldValues = new ArrayList<>();

                for (TextField f : textFields) {
                    fieldValues.add(f.getText());
                }
                itemContainer.get(selectedItem.getValue()).setFieldValues(fieldValues);
            } catch (ItemNotExistException e) {
                throw new RuntimeException(e);
            }
            dialog.setDialogPane(pane);
            dialog.showAndWait();
        } else { // if selectedItem is type
        }
    }

    private void onDelete() {
        TreeItem<String> selectedItem = view.getSelectionModel().getSelectedItem();
        if (selectedItem.isLeaf()) { // if selectedItem is item
            try {
                Item item = itemContainer.get(selectedItem.getValue());
                itemContainer.remove(item);

                item.getType().getNode().getChildren().removeIf(i -> i.getValue().equals(item.getName()));
            } catch (ItemNotExistException e) {
                e.printStackTrace();
            }
        } else { // if selectedItem is type
            try {
                typeContainer.remove(typeContainer.get(selectedItem.getValue()));
                refresh();
            } catch (TypeNotExistException e) {
                e.printStackTrace();
            }
        }
    }

    private void onHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("help");
        alert.setHeaderText("""
                Lorem ipsum dolor sit amet consectetur adipiscing elit,
                urna consequat felis vehicula class ultricies mollis dictumst.
                """);
        alert.show();
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
        selectTypeBox.getItems().clear();

        for (Type type : typeContainer.getAll()) {
            view.getRoot().getChildren().add(type.getNode());
            selectTypeBox.getItems().add(type.getName());
        }
    }
}
