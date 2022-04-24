package com.example.catalog.exception.item;

import com.example.catalog.exception.CatalogException;

public class ItemNotExistException extends CatalogException {
    public ItemNotExistException() {
        super("item does not exist");
    }
}