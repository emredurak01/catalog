package com.example.catalog.exception.tag;

import com.example.catalog.exception.CatalogException;

public class TagExistException extends CatalogException {
    public TagExistException() {
        super("tag exists");
    }
}