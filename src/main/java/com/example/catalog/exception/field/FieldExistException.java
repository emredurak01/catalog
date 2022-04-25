package com.example.catalog.exception.field;

import com.example.catalog.exception.CatalogException;

public class FieldExistException extends CatalogException {
    public FieldExistException() {
        super("Field already exist");
    }
}