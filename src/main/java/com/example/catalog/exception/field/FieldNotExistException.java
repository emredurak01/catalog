package com.example.catalog.exception.field;

import com.example.catalog.exception.CatalogException;

public class FieldNotExistException extends CatalogException {
    public FieldNotExistException() {
        super("field does not exist");
    }
}