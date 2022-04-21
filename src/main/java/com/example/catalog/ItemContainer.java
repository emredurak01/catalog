package com.example.catalog;

import com.example.catalog.exception.item.ItemExistException;
import com.example.catalog.exception.item.ItemNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class ItemContainer {

    private final List<Item> items;

    public ItemContainer(List<Item> items) {
        this.items = items;
    }

    public ItemContainer() {
        items = new ArrayList<>();
    }

    public List<Item> getAll() {
        return items;
    }

    public void addItem(Item item) throws ItemExistException {
        try {
            get(item.getName());
            throw new ItemExistException();
        } catch (ItemNotExistException e) {
            items.add(item);
        }
    }

    public Item get(String name) throws ItemNotExistException {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        throw new ItemNotExistException();

    }

    public void removeItem(Item item) throws ItemNotExistException {

        if (!items.contains(item)) {
            throw new ItemNotExistException();
        }
        items.remove(item);
    }

    public List<Item> getByTags(Set<String> tags) {
        List<Item> items = new ArrayList<>(); // temporary
        for (Item item : getAll()) { // getAll() returns the stored items
            if (item.getTags().containsAll(tags)) {
                items.add(item);
            }

        }
        return items;

    }
}