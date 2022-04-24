package com.example.catalog.exception.item;

import com.example.catalog.exception.CatalogException;

public class ItemExistException extends CatalogException {
    public ItemExistException() {
        super("item exists");
    }
}