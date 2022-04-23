package com.example.catalog;


import com.example.catalog.exception.field.TypeExistException;
import com.example.catalog.exception.field.FieldNotExistException;
import javafx.scene.control.TreeItem;

import java.util.List;

public class Type {

    private String name;
    private final List<String> fieldValues;

    /**
     * stores nodes of items of this type
     */
    private final TreeItem<String> node;

    /**

     * @param name        key

     * @param fieldValues field types
     */
    public Type(String name, List<String> fieldValues) {
        this.name = name;
        this.fieldValues = fieldValues;
        node = new TreeItem<>(name);
    }

    /**
     * @param field name of non-existing field type to be added to fields
     * @throws TypeExistException if fields exists
     */
    public void addField(String field) throws TypeExistException {

        if (fieldValues.contains(field)) {

            throw new TypeExistException();
        }
        fieldValues.add(field);
    }

    /**
     * @param field name of existing field type to be removed from fields
     * @throws FieldNotExistException if field does not exist
     */
    public void removeField(String field) throws FieldNotExistException {

        if (!fieldValues.contains(field)) {

            throw new FieldNotExistException();
        }
        fieldValues.remove(field);
    }

    public void editName(String newName) {
        setName(newName);
    }

    /**
     * @param name    old field name
     * @param newName new field name
     * @throws FieldNotExistException if field with old name does not exist
     * @throws TypeExistException     if field with new name exists
     */
    public void editField(String name, String newName) throws FieldNotExistException, TypeExistException {
        int index = fieldValues.indexOf(name);

        if (index == -1) {
            throw new FieldNotExistException();
        }


        if (fieldValues.contains(newName)) {

            throw new TypeExistException();
        }
        fieldValues.set(index, newName);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getFieldValues() {
        return fieldValues;
    }

    public TreeItem<String> getNode() {
        return node;
    }

}