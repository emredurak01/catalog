package com.example.catalog.exception.type;

import com.example.catalog.exception.CatalogException;

public class TypeNotExistException extends CatalogException {
    public TypeNotExistException() {
        super("type does not exist");
    }
}