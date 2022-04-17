package com.example.catalog;


import com.example.catalog.exception.field.FieldExistException;
import com.example.catalog.exception.field.FieldNotExistException;
import javafx.scene.control.TreeItem;
import java.util.List;

public class Type {

    private String name;


    private final List<String> fields;

    /**
     * stores nodes of items of this type
     */
    private final TreeItem<String> node;

    /**
     * @param name key
     * @param fields field types
     */
    public Type(String name, List<String> fields) {
        this.name = name;
        this.fields = fields;
        node = new TreeItem<>(name);
    }

    /**
     * @param field name of on-existing field type to be added to fields
     * @throws FieldExistException if fields exists
     */
    public void addField(String field) throws FieldExistException {
        if(fields.contains(field)) {
            throw new FieldExistException();
        }
        fields.add(field);
    }

    /**
     * @param field name of existing field type to be removed from fields
     * @throws FieldNotExistException if field does not exist
     */
    public void removeField(String field) throws FieldNotExistException {
        if(!fields.contains(field)) {
            throw new FieldNotExistException();
        }
        fields.remove(field);
    }
    public void editName(String newName) {
        setName(newName);
    }

    public void editField(String oldField, String newField) throws FieldExistException {
        for(int i = 0; i < fields.size(); i++) {
            if(fields.contains(name) || fields.contains(newField)) {
                throw new FieldExistException();
            }
            else if(fields.contains(oldField)) {
                fields.set(i, newField);
            }
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getFields() {
        return fields;
    }

    public TreeItem<String> getNode() {
        return node;
    }

}
