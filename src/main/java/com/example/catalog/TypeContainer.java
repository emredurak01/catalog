package com.example.catalog;


import com.example.catalog.exception.type.TypeExistException;
import com.example.catalog.exception.type.TypeNotExistException;
import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.List;

public class TypeContainer {

    private final List<Type> types;

    public TypeContainer() {
        types = new ArrayList<>();
    }

    public void add(Type type, TreeView<String> view) throws TypeExistException {
        try {
            get(type.getName());
            throw new TypeExistException();
        } catch (TypeNotExistException e) {
            types.add(type);
            view.getRoot().getChildren().add(type);
        }
    }

    public void remove(Type type, TreeView<String> view, ItemContainer itemContainer) throws TypeNotExistException {
        if (!types.contains(type)) {
            throw new TypeNotExistException();
        }
        types.remove(type);
        view.getRoot().getChildren().remove(type);
        itemContainer.getAll().removeIf(t -> t.getType().getName().equals(type.getName()));
    }

    public void get(String name) throws TypeNotExistException {
        for (Type type : types) {
            if (type.getName().equals(name)) {
                return;
            }
        }
        throw new TypeNotExistException();
    }
}