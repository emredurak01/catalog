package com.example.catalog;

import com.example.catalog.exception.field.FieldExistException;
import com.example.catalog.exception.field.FieldNotExistException;
import javafx.scene.control.TreeItem;

import java.util.List;

public class Type {
    public static final String PREFIX = "/";
    private String name;
    private List<String> fieldTypes;

    /**
     * stores nodes of items of this type
     */
    private final TreeItem<String> node;

    /**
     * @param name        key
     * @param fieldTypes field types
     */
    public Type(String name, List<String> fieldTypes) {
        this.name = name;
        this.fieldTypes = fieldTypes;
        node = new TreeItem<>(name);
    }

    /**
     * @param field name of non-existing field type to be added to fields
     * @throws FieldExistException if fields exists
     */
    public void addField(String field) throws FieldExistException {
        if (fieldTypes.contains(field)) {
            throw new FieldExistException();
        }
        fieldTypes.add(field);
    }

    /**
     * @param field name of existing field type to be removed from fields
     * @throws FieldNotExistException if field does not exist
     */
    public void removeField(String field) throws FieldNotExistException {
        if (!fieldTypes.contains(field)) {
            throw new FieldNotExistException();
        }
        fieldTypes.remove(field);
    }

    /**
     * @param name    old field name
     * @param newName new field name
     * @throws FieldNotExistException if field with old name does not exist
     * @throws FieldExistException    if field with new name exists
     */
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
        node.setValue(name);
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

    public TreeItem<String> getNode() {
        return node;
    }
}