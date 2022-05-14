package com.example.catalog.type;

import com.example.catalog.exception.field.FieldExistException;
import com.example.catalog.exception.field.FieldNotExistException;
import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Type extends TreeItem<String> {
    private final UUID id;
    private String name;
    private List<String> fieldTypes;

    public Type(String name, List<String> fieldTypes) {
        id = UUID.randomUUID();
        setName(name);
        this.fieldTypes = fieldTypes;
    }

    public UUID getId() {
        return id;
    }

    public void addField(String field) throws FieldExistException {
        if (fieldTypes.contains(field)) {
            throw new FieldExistException();
        }
        fieldTypes.add(field);
    }

    public void removeField(String field) throws FieldNotExistException {
        if (!fieldTypes.contains(field)) {
            throw new FieldNotExistException();
        }
        fieldTypes.remove(field);
    }

    public void editField(String name, String newName) throws FieldNotExistException, FieldExistException {
        int index = fieldTypes.indexOf(name);

        if (index == -1) {
            throw new FieldNotExistException();
        }

        if (fieldTypes.contains(newName)) {
            throw new FieldExistException();
        }
        fieldTypes.set(index, newName);
    }

    public void setName(String name) {
        this.name = name;
        setValue(name);
    }

    public String getName() {
        return name;
    }

    public List<String> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(List<String> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }
}