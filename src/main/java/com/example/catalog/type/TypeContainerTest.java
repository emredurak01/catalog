package com.example.catalog.type;

import com.example.catalog.exception.type.TypeExistException;
import com.example.catalog.exception.type.TypeNotExistException;
import com.example.catalog.item.Item;
import com.example.catalog.item.ItemContainer;
import javafx.application.Application;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TypeContainerTest {
    public static class App extends Application {
        @Override
        public void start(Stage primaryStage) {
        }
    }

    @BeforeClass
    public static void initJFX() {
        Thread t = new Thread("JavaFX") {
            public void run() {
                Application.launch(App.class);
            }
        };
        t.setDaemon(true);
        t.start();
    }

    @Test
    public void getAll() throws TypeExistException, TypeNotExistException {
        TypeContainer typeContainer = new TypeContainer();
        List<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("title");
        fieldTypes.add("author");
        fieldTypes.add("date");
        TreeView<String> view = new TreeView<>();
        view.setRoot(new TreeItem<>());
        Type type = new Type("book", fieldTypes);
        typeContainer.add(type, view);
        assert typeContainer.getAll().size() == 1 && typeContainer.get(type.getName()).equals(type) &&
                view.getRoot().getChildren().get(0).equals(type);
    }

    @Test
    public void add() throws TypeExistException, TypeNotExistException {
        // create a type container with no types in it initially
        TypeContainer typeContainer = new TypeContainer();
        TreeView<String> view = new TreeView<>();
        view.setRoot(new TreeItem<>());
        List<String> fieldTypes0 = new ArrayList<>();
        fieldTypes0.add("title");
        fieldTypes0.add("author");
        fieldTypes0.add("date");
        Type type = new Type("book0", fieldTypes0);
        typeContainer.add(type, view);
        // the item should be added correctly, thus the size increase to 1
        assert typeContainer.getAll().size() == 1 && typeContainer.get(type.getName()).equals(type) &&
                view.getRoot().getChildren().get(0).equals(type);

        typeContainer.add(new Type("", fieldTypes0), view);
        typeContainer.add(new Type(" ", fieldTypes0), view);
        // the size should not change, since the types with empty names and blank names are not allowed to be added
        assert typeContainer.getAll().size() == 1;
        List<String> fieldTypes1 = new ArrayList<>();
        fieldTypes1.add("title");
        fieldTypes1.add("");
        typeContainer.add(new Type("book1", fieldTypes1), view);
        // the size should not change, since the types with empty tags are not allowed to be added
        assert typeContainer.getAll().size() == 1;
        List<String> fieldTypes2 = new ArrayList<>();
        fieldTypes2.add("title");
        fieldTypes2.add(" ");
        typeContainer.add(new Type("book2", fieldTypes2), view);
        // the size should not change, since the blank tags are not allowed to be added
        assert typeContainer.getAll().size() == 1;
    }

    @Test
    public void remove() throws TypeNotExistException, TypeExistException {
        TypeContainer typeContainer = new TypeContainer();
        List<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("title");
        fieldTypes.add("author");
        fieldTypes.add("date");
        TreeView<String> view = new TreeView<>();
        view.setRoot(new TreeItem<>());
        Type type = new Type("book", fieldTypes);
        typeContainer.add(type, view);
        List<String> fieldValues = new ArrayList<>();
        fieldValues.add("star wars");
        fieldValues.add("obi wan");
        fieldValues.add("a long time ago");
        String itemName = "hello there";
        String typeName = "book";
        Item item = new Item(itemName, new Type(typeName, fieldTypes), fieldValues);
        ItemContainer itemContainer = new ItemContainer();
        itemContainer.add(item);
        typeContainer.remove(type, view, itemContainer);
        assert typeContainer.getAll().size() == 0 && itemContainer.getAll().size() == 0;
    }

    @Test
    public void get() throws TypeNotExistException, TypeExistException {
        TypeContainer typeContainer = new TypeContainer();
        TreeView<String> view = new TreeView<>();
        view.setRoot(new TreeItem<>());
        List<String> fieldTypes0 = new ArrayList<>();
        fieldTypes0.add("title");
        fieldTypes0.add("author");
        fieldTypes0.add("date");
        Type type = new Type("book", fieldTypes0);
        typeContainer.add(type, view);
        assert typeContainer.get(type.getName()).getName().equals(type.getName());
    }
}