package com.example.catalog;


import com.example.catalog.exception.type.TypeExistException;
import com.example.catalog.exception.type.TypeNotExistException;
import java.util.ArrayList;
import java.util.List;

public class Types {

    /**
     * list to store types
     */
    private final List<Type> types;

    /**
     * types is initially empty
     */
    public Types() {
        types = new ArrayList<>();
    }

    /**
     * @return all types
     */

    public List<Type> getAll() {
        return types;
    }


    /**
     * @param type non-existing type to be added to types
     * @throws TypeExistException if type exists
     */
    public void add(Type type) throws TypeExistException {
        try {
            get(type.getName());
            throw new TypeExistException();
        } catch (TypeNotExistException e) {

            types.add(type);
        }
    }


    /**
     * @param type existing type to be removed from types
     * @throws TypeNotExistException if type does not exist
     */
    public void remove(Type type) throws TypeNotExistException {
        if(!types.contains(type)) {
            throw new TypeNotExistException();
        }
        types.remove(type);
    }

    /**
     * @param name existing type name to search in types
     * @return type by name
     * @throws TypeNotExistException if type does not exist
     */
    public Type get(String name) throws TypeNotExistException {
        for (Type type : types) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        throw new TypeNotExistException();

    }
}
