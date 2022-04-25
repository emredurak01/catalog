package com.example.catalog.exception.tag;

import com.example.catalog.exception.CatalogException;

public class TagNotExistException extends CatalogException {
    public TagNotExistException() {
        super("Tag does not exist");
    }
}