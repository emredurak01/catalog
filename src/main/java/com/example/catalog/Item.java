package com.example.catalog;

import com.example.catalog.exception.item.ItemExistException;
import com.example.catalog.exception.item.ItemNotExistException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Item {

    private String name;
    private Type type;
    private Set<String> tags; //setter getter
    private List<String> fieldValues;


    public Item(String name, Type type, List<String> fieldValues) {
        this.name = name;
        this.type = type;
        this.fieldValues = fieldValues;
        tags = new HashSet<>();
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


    public void editName(String newName) {

        setName(newName);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public List<String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<String> fieldValues) {
        this.fieldValues = fieldValues;
    }


}
