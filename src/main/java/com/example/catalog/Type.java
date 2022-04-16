package com.example.catalog;

import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class Type {

    private String name;
    private List<String> fields;
    TreeItem<String> node;
    public Type(String name) {
        this.name = name;
        fields = new ArrayList<>();
    }

    public void addField(String field) {
        if(fields.contains(field)) {
            System.out.println("This field already exists.");
        } else {
            fields.add(field);
        }
    }
    public void removeField(String field) {
        if(!fields.contains(field)) {
            System.out.println("This field does not exists.");
        } else {
            fields.remove(field);
        }

    }
    public String getName() {
        return name;
    }

    public List<String> getFields() {
        return fields;
    }
}
