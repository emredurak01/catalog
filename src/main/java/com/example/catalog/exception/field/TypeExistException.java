package com.example.catalog.exception.field;

import com.example.catalog.exception.CatalogException;

public class TypeExistException extends CatalogException {

    public TypeExistException() {
        super("field exists");
    }
}
