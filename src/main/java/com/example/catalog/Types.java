package com.example.catalog;

import java.util.ArrayList;
import java.util.List;

public class Types {
    private List<Type> types;
    private Type type;
    public Types(String name) {
        this.type = new Type(name);
        types = new ArrayList<>();
    }

    public List<Type> getAll() {
        return types;
    }

    public void addType(Type type) {
        if(types.contains(type)) {
            System.out.println("This type already exists.");
        } else {
            types.add(type);
        }
    }

    public void removeType(Type type) {
        if(!types.contains(type)) {
            System.out.println("This type does not exists.");
        } else {
            types.remove(type);
        }

    }

    public Type getType() {
        return type;
    }
}
