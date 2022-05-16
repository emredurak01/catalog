package com.example.catalog;

import com.example.catalog.exception.item.ItemNotExistException;
import com.example.catalog.exception.tag.TagExistException;
import com.example.catalog.exception.type.TypeExistException;
import com.example.catalog.exception.type.TypeNotExistException;
import com.example.catalog.item.Item;
import com.example.catalog.item.ItemContainer;
import com.example.catalog.type.Type;
import com.example.catalog.type.TypeContainer;
import com.example.catalog.util.Localisation;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CatalogController {
    private final TypeContainer typeContainer = new TypeContainer();
    private final ItemContainer itemContainer = new ItemContainer();
    private final List<String> tags = new ArrayList<>();
    private final Set<String> selectedTags = new HashSet<>();
    @FXML
    private Button helpButton;
    @FXML
    private Button exitButton;
    @FXML
    private TreeView<String> view;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button addTagButton;
    @FXML
    private TextField tagField;
    @FXML
    private TableView<List<String>> table;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem printMenuItem;
    @FXML
    private MenuItem exportMenuItem;
    @FXML
    private Button saveButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button exportButton;
    @FXML
    private Button printButton;
    @FXML
    private VBox tagBox;
    private final List<Item> removedItems = new ArrayList<>();

    @FXML
    private void initialize() {
        saveMenuItem.setOnAction(event -> onSave());
        saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        exportMenuItem.setOnAction(event -> onExport());
        exportMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        printMenuItem.setOnAction(event -> onPrint());
        printMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
        closeMenuItem.setOnAction(event -> onExit());
        closeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        aboutMenuItem.setOnAction(event -> onHelp());
        aboutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(true);
        view.setRoot(root);
        view.getSelectionModel().select(root);
        typeContainer.read(view);
        itemContainer.read(typeContainer);
        tagRefresh();
        helpButton.setOnAction((actionEvent -> onHelp()));
        exitButton.setOnAction(actionEvent -> onExit());
        view.setOnMouseClicked(mouseEvent -> onSelect());
        addButton.setOnAction((actionEvent -> onAdd()));
        editButton.setOnAction(actionEvent -> onEdit());
        removeButton.setOnAction(actionEvent -> onRemove());
        addTagButton.setOnAction(actionEvent -> onAddTag());
        saveButton.setOnAction(event -> onSave());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> onSearch(newValue));
        exportButton.setOnAction(event -> onExport());
        printButton.setOnAction(event -> onPrint());
    }

    private void onPrint() {
        view.getRoot().setExpanded(true);
        view.getRoot().getChildren().forEach(child -> child.setExpanded(true));

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job.showPrintDialog(view.getScene().getWindow())){
            boolean success = job.printPage(view);

            if (success) {
                job.endJob();
            }
        }
    }

    private void onExport() {
        StringBuilder s = new StringBuilder();
        s.append("<!doctype html>\n");
        s.append("<html lang=\"en-gb\">\n");
        s.append("<head>\n");
        s.append("<title>catalog</title>\n");
        s.append("</head>\n");
        s.append("<body>\n");

        for (Type type : typeContainer.getAll()) {
            s.append("<li id=\"").append(type.getName()).append("\">\n");
            s.append(type.getName()).append("\n");

            for (Item item : itemContainer.getAll()) {
                if (item.getType().equals(type)) {
                    s.append("<ul id=\"").append(item.getName()).append("\">\n");
                    s.append(item.getName());
                    s.append(" {");

                    for (int i = 0; i < item.getFieldValues().size(); i++) {
                        s.append(item.getType().getFieldTypes().get(i)).append(": ").append(item.getFieldValues().get(i)).append(", ");
                    }
                    s.append("tags: [");

                    for (String tag : item.getTags()) {
                        s.append(tag).append(", ");
                    }
                    s = new StringBuilder(s.substring(0, s.length() - 2));
                    s.append("]");
                    s.append("}");
                    s.append("\n</ul>\n");
                }
            }
            s.append("\n</li>\n");
        }
        s.append("</body>\n");
        s.append("</html>\n");
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("html file", "*.html");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showSaveDialog(view.getScene().getWindow());

        if (file != null) {
            File f = new File(file.getPath());

            if (!file.getName().contains(".")) {
                f = new File(f.getPath() + ".html");
            }

            try {
                Files.writeString(Path.of(f.getPath()), s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSave() {
        Alert alert;

        try {
            typeContainer.write();
            itemContainer.write();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(Localisation.SAVE_SUCCESS);
            alert.show();
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    private void onSearch(String value) {
        for (Item item : removedItems) {
            item.getType().getChildren().add(item);
        }
        removedItems.clear();

        List<Item> filteredItems = new ArrayList<>(itemContainer.getAll());
        view.getRoot().getChildren().clear();

        for (Type type : typeContainer.getAll()) {
            if (type.getName().contains(value)) {
                view.getRoot().getChildren().add(type);
            }
        }

        for (Item item : itemContainer.getAll()) {
            if (!item.getName().contains(value) && !isInView(item.getType().getName()) && !item.getTags().contains(value) || !itemContainer.getByTags(selectedTags).contains(item)) {
                item.getType().getChildren().remove(item);
                filteredItems.remove(item);
                removedItems.add(item);
            }
        }

        for (Item item : filteredItems) {
            if (!isInView(item.getType().getName())) {
                view.getRoot().getChildren().add(item.getType());
            }
        }
        view.getRoot().getChildren().removeIf(type -> type.getChildren().isEmpty());
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
            TableColumn<List<String>, String> column = new TableColumn<>("tags");
            column.setCellValueFactory(e -> new SimpleStringProperty(item.getTags().toString()));
            table.getColumns().add(column);
            List<String> fieldValues = FXCollections.observableArrayList();
            fieldValues.addAll(item.getFieldValues());
            table.getItems().add(fieldValues);
        }
    }

    @FXML
    private void onHelp() {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("help.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.setScene(new Scene(parent, 600, 400));
            stage.setMinWidth(605);
            stage.setMinHeight(405);
            stage.setResizable(false);


            stage.show
                    ();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("help fail");
            System.out.println("**********************"+e);

            alert.show();
        }
    }

    private void onExit() {
        Platform.exit();
    }

    public void onAdd() {
        TreeItem<String> treeItem = view.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (treeItem instanceof Item) {
            alert.setHeaderText(Localisation.SELECT + " " + Localisation.ROOT + " or " + Localisation.TYPE);
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
        pane.getButtonTypes().addAll(ButtonType.OK);
        dialog.setDialogPane(pane);
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(windowEvent -> dialog.close());
        GridPane content = new GridPane();
        pane.setContent(content);
        TextField name = new TextField(type.getName());
        content.add(new Label("Name"), 0, 0);
        content.add(name, 1, 0);
        List<TextField> textFields = new ArrayList<>();

        for (int i = 0; i < type.getFieldTypes().size(); i++) {
            textFields.add(new TextField());
        }

        for (int i = 0; i < textFields.size(); i++) {
            content.add(new Label(type.getFieldTypes().get(i)), 0, i + 2);
            content.add(textFields.get(i), 1, i + 2);
        }
        dialog.setResultConverter(buttonType -> {
            if (buttonType == null) {
                return null;
            }
            List<String> fieldValues = new ArrayList<>();

            for (TextField f : textFields) {
                if(f.getText().contains(",")) {
                    alert.setHeaderText(Localisation.USED_COMMA);
                    alert.show();
                    return null;
                } else {
                    fieldValues.add(f.getText());
                }
            }
            return new Item(name.getText(), type, fieldValues);
        });
        Optional<Item> result = dialog.showAndWait();

        if (result.isPresent()) {
            Item item = result.get();

            if (item.getName().trim().isEmpty()) {
                alert.setHeaderText(Localisation.EMPTY_NAME);
                alert.show();
            } else if(item.getName().contains(",")) {
                alert.setHeaderText(Localisation.USED_COMMA);
                alert.show();
            }
            else {
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
        fields.setPromptText("field1, field2, ...");
        content.add(new Label("Name"), 0, 0);
        content.add(name, 1, 0);
        content.add(new Label("Field types"), 0, 1);
        content.add(fields, 1, 1);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == null) {
                return null;
            }

            if (fields.getText().isBlank()) {
                alert.setHeaderText(Localisation.BLANK_FIELD);
                alert.show();
                return null;
            }
            List<String> fieldTypes = new ArrayList<>(List.of(fields.getText().split(",")));
            fieldTypes.replaceAll(String::trim);
            return new Type(name.getText(), fieldTypes);
        });
        dialog.setDialogPane(pane);
        Optional<Type> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                Type type = result.get();

                if (type.getName().trim().isEmpty()) {
                    alert.setHeaderText(Localisation.EMPTY_NAME);
                    alert.show();
                } else if(type.getName().contains(",")) {
                    alert.setHeaderText(Localisation.USED_COMMA);
                    alert.show();
                }

                else {
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
        Dialog<ButtonType> dialog = new Dialog<>();
        DialogPane pane = new DialogPane();
        pane.getButtonTypes().addAll(ButtonType.OK);
        dialog.setDialogPane(pane);
        GridPane content = new GridPane();
        pane.setContent(content);
        content.add(new Label("Name"), 0, 0);
        TextField name = new TextField();
        content.add(name, 1, 0);
        content.add(new Label(), 2, 0);
        List<TextField> textFields = new ArrayList<>();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (treeItem instanceof Type type) {
            ArrayList<Integer> removedIndices = new ArrayList<>();
            AtomicInteger addedFieldCounter = new AtomicInteger();
            dialog.setTitle("Edit type");
            name.setText(type.getName());

            for (int i = 0; i < type.getFieldTypes().size(); i++) {
                textFields.add(new TextField(type.getFieldTypes().get(i)));
            }

            for (int i = 0; i < textFields.size(); i++) {
                Label label = new Label("Field type ");
                content.add(label, 0, i + 2);
                TextField textField = textFields.get(i);
                content.add(textField, 1, i + 2);
                Button button = new Button(Localisation.REMOVE_BUTTON);
                button.setOnAction(actionEvent -> {
                    content.getChildren().remove(label);
                    content.getChildren().remove(textField);
                    removedIndices.add(textFields.indexOf(textField));
                    // addedFieldCounter.getAndDecrement();
                    textFields.remove(textField);
                    content.getChildren().remove(button);
                });
                content.add(button, 2, i + 2);
            }
            Button addButton = new Button(Localisation.ADD_BUTTON);
            addButton.setOnAction(actionEvent -> {
                TextField textField = new TextField();
                textFields.add(textField);
                content.addColumn(1, textField);
                Label fieldTypeLabel = new Label("Field type");
                content.addColumn(0, fieldTypeLabel);
                pane.getScene().getWindow().sizeToScene();
                Button removeFieldButton = new Button(Localisation.REMOVE_BUTTON);
                removeFieldButton.setOnAction(event -> {
                    content.getChildren().remove(fieldTypeLabel);
                    content.getChildren().remove(textField);
                    removedIndices.add(textFields.indexOf(textField));
                    // addedFieldCounter.getAndDecrement();
                    textFields.remove(textField);
                    content.getChildren().remove(removeFieldButton);
                });
                content.getChildren().remove(addButton);
                content.addColumn(2, removeFieldButton);
                content.addColumn(2, addButton);
                addedFieldCounter.getAndIncrement();
            });
            content.add(addButton, 2, textFields.size() + 2);
            dialog.setResultConverter(buttonType -> buttonType);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                if (name.getText().trim().isEmpty()) {
                    alert.setHeaderText(Localisation.EMPTY_NAME);
                    alert.show();
                } else {
                    if (name.getText().contains(",")) {
                        alert.setHeaderText(Localisation.USED_COMMA + " in name");
                        alert.show();
                        return;
                    }
                    type.setName(name.getText());
                    List<String> fieldTypes = new ArrayList<>();

                    for (TextField f : textFields) {
                        if (f.getText().isBlank()) {
                            alert.setHeaderText(Localisation.BLANK_FIELD);
                            alert.show();
                            return;
                        }
                        if (f.getText().contains(",")) {
                            alert.setHeaderText(Localisation.USED_COMMA + " in fields");
                            alert.show();
                            return;
                        }
                        fieldTypes.add(f.getText().trim());
                    }
                    type.setFieldTypes(fieldTypes);

                    for (Item item : itemContainer.getAll()) {
                        if (item.getType().equals(type)) {
                            for (Integer removedIndex : removedIndices) {
                                try {
                                    item.getFieldValues().set(removedIndex, null);
                                } catch (Exception e) {
                                    System.out.println("aaaaaaaaaaaaaaaaa");
                                }

                            }
                            item.getFieldValues().removeIf(Objects::isNull);

                            for (int i = 0; i < addedFieldCounter.get(); i++) {
                                item.getFieldValues().add("");
                            }
                        }
                    }
                    onSelect();
                }
            }
        } else if (treeItem instanceof Item item) {
            dialog.setTitle("Edit item");
            name.setText(item.getName());

            for (int i = 0; i < item.getFieldValues().size(); i++) {
                textFields.add(new TextField(item.getFieldValues().get(i)));
            }

            for (int i = 0; i < textFields.size(); i++) {
                content.add(new Label(item.getType().getFieldTypes().get(i)), 0, i + 2);
                content.add(textFields.get(i), 1, i + 2);
            }
            content.add(new Label("Tags"), 0, textFields.size() + 2);
            String itemTags = item.getTags().toString();
            TextField tagsField = new TextField(itemTags.substring(1, itemTags.length() - 1));
            tagsField.setPromptText("tag1, tag2, ...");
            content.add(tagsField, 1, textFields.size() + 2);
            dialog.setResultConverter(buttonType -> buttonType);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                if (name.getText().trim().isEmpty()) {
                    alert.setHeaderText(Localisation.EMPTY_NAME);
                    alert.show();
                } else {
                    if (name.getText().contains(",")) {
                        alert.setHeaderText(Localisation.USED_COMMA + " in name");
                        alert.show();
                        return;
                    }
                    item.setName(name.getText());
                    List<String> fieldValues = new ArrayList<>();

                    for (TextField f : textFields) {
                        if (f.getText().contains(",")) {
                            alert.setHeaderText(Localisation.USED_COMMA + " in fields");
                            alert.show();
                            return;
                        }
                        fieldValues.add(f.getText());
                    }
                    item.setFieldValues(fieldValues);
                    Set<String> tags2 = new HashSet<>();
                    String[] trimmedTags = tagsField.getText().split(",");

                    for (int i = 0; i < trimmedTags.length; i++) {
                        trimmedTags[i] = trimmedTags[i].trim();

                        if (trimmedTags[i].isBlank() && !tagsField.getText().isBlank()) {
                            alert.setHeaderText(Localisation.EMPTY_TAG);
                            alert.show();
                            return;
                        }
                    }

                    if (!tagsField.getText().isBlank()) {
                        Collections.addAll(tags2, trimmedTags);
                        item.setTags(tags2);
                        tagRefresh();
                    } else {
                        item.setTags(new HashSet<>());
                        tagRefresh();
                    }
                    onSelect();
                }
            }
        } else {
            alert.setHeaderText(Localisation.SELECTED_ROOT);
            alert.show();
        }
    }

    private void tagRefresh() {
        tags.clear();

        for (Item i : itemContainer.getAll()) {
            i.getTags().forEach(tag -> {
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
            });
        }
        tagBox.getChildren().clear();

        for (int i = 0; i < tags.size(); i++) {
            CheckBox checkBox = new CheckBox();
            int finalI = i;
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    selectedTags.add(tags.get(finalI));
                } else {
                    selectedTags.remove(tags.get(finalI));
                }
                onSearch(searchField.getText());
            });
            HBox hbox = new HBox(5);
            hbox.setPadding(new Insets(1));
            hbox.getChildren().addAll(checkBox, new Label(tags.get(i)));
            tagBox.getChildren().addAll(hbox);
        }
    }

    private void onRemove() {
        TreeItem<String> treeItem = view.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (treeItem instanceof Type type) {
            try {
                typeContainer.remove(type, view, itemContainer);
                tagRefresh();
                onSelect();
            } catch (TypeNotExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        } else if (treeItem instanceof Item item) {
            try {
                itemContainer.remove(item);
                tagRefresh();
                onSelect();
            } catch (ItemNotExistException e) {
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        } else {
            alert.setHeaderText(Localisation.SELECTED_ROOT);
            alert.show();
        }
    }

    private void onAddTag() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (tagField.getText().isBlank()) {
            alert.setHeaderText(Localisation.BLANK_TAG);
            alert.show();
        } else if (tagField.getText().contains(",")) {
            alert.setHeaderText(Localisation.USED_COMMA);
            alert.show();
        } else {
            TreeItem<String> treeItem = view.getSelectionModel().getSelectedItem();

            if (treeItem instanceof Item item) {
                try {
                    item.addTag(tagField.getText());

                    if (!tags.contains(tagField.getText())) {
                        CheckBox checkBox = new CheckBox();
                        String tag = tagField.getText();
                        checkBox.setOnAction(event -> {
                            if (checkBox.isSelected()) {
                                selectedTags.add(tag);
                            } else {
                                selectedTags.remove(tag);
                            }
                            onSearch(searchField.getText());
                        });
                        HBox hbox = new HBox(5);
                        hbox.setPadding(new Insets(1));
                        hbox.getChildren().addAll(checkBox, new Label(tag));
                        tagBox.getChildren().addAll(hbox);
                    }
                    onSelect();
                } catch (TagExistException e) {
                    alert.setHeaderText(e.getMessage());
                    alert.show();
                }
            } else if (treeItem instanceof Type) {
                alert.setHeaderText(Localisation.SELECTED_TYPE);
                alert.show();
            } else {
                alert.setHeaderText(Localisation.SELECTED_ROOT);
                alert.show();
            }
            tagField.clear();
        }
    }
    private boolean isInView(String value) {
        for (TreeItem<String> treeItem : view.getRoot().getChildren()) {
            if (treeItem.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}