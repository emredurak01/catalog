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
}