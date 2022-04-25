package com.example.catalog;

import com.example.catalog.exception.item.ItemNotExistException;
import com.example.catalog.exception.tag.TagExistException;
import com.example.catalog.exception.tag.TagNotExistException;
import javafx.scene.control.TreeItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Item extends TreeItem<String>{
    private final UUID id;
    private String name;
    private Type type;
    private List<String> fieldValues;
    private final Set<String> tags;

    public Item(String name, Type type, List<String> fieldValues) {
        id = UUID.randomUUID();
        setName(name);
        this.type = type;
        this.fieldValues = fieldValues;
        tags = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setValue(name);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void editField(String name, String newName) throws ItemNotExistException {
        int index = fieldValues.indexOf(name);

        if (index == -1) {
            throw new ItemNotExistException();
        }
        fieldValues.set(index, newName);
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTag(String tag) throws TagExistException {
        if (tags.contains(tag)) {
            throw new TagExistException();
        }

        tags.add(tag);
    }

    public void removeTag(String tag) throws TagNotExistException {
        if (!tags.contains(tag)) {
            throw new TagNotExistException();
        }
        tags.remove(tag);
    }

    public List<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<String> fieldValues) {
        this.fieldValues = fieldValues;
    }
}