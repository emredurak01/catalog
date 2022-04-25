package com.example.catalog.exception.type;

import com.example.catalog.exception.CatalogException;

public class TypeExistException extends CatalogException {
    public TypeExistException() {
        super("Type already exists");
    }
}