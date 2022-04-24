package com.example.catalog;

import com.example.catalog.exception.item.ItemExistException;
import com.example.catalog.exception.item.ItemNotExistException;
import javafx.scene.control.TreeItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Item {
    private String name;
    private Type type;
    private List<String> fieldValues;
    private final Set<String> tags;

    private final TreeItem<String> node;

    public Item(String name, Type type, List<String> fieldValues) {
        this.name = name;
        this.type = type;
        this.fieldValues = fieldValues;
        tags = new HashSet<>();
        node = new TreeItem<>(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        node.setValue(name);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void editField(String name, String newName) throws ItemNotExistException, ItemExistException {
        int index = fieldValues.indexOf(name);

        if (index == -1) {
            throw new ItemNotExistException();
        }

        if (fieldValues.contains(newName)) {
            throw new ItemExistException();
        }
        fieldValues.set(index, newName);
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTag(String tag) throws ItemExistException {
        if (tags.contains(tags)) {
            throw new ItemExistException();
        }

        tags.add(tag);
    }

    public void removeTag(String tag) throws ItemNotExistException {
        if (!tags.contains(tag)) {
            throw new ItemNotExistException();
        }
        tags.remove(tag);
    }

    public List<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public TreeItem<String> getNode() {
        return node;
    }
}