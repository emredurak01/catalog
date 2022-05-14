package com.example.catalog.item;

import com.example.catalog.exception.item.ItemNotExistException;
import com.example.catalog.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemContainer {
    private final List<Item> items;

    public ItemContainer() {
        items = new ArrayList<>();
    }

    public List<Item> getAll() {
        return items;
    }

    public void add(Item item) {
        items.add(item);
        item.getType().getChildren().add(item);
    }

    public void remove(Item item) throws ItemNotExistException {
        if (!items.contains(item)) {
            throw new ItemNotExistException();
        }
        items.remove(item);
        item.getType().getChildren().remove(item);
    }

    public List<Item> getByTags(Set<String> tags) {
        List<Item> items = new ArrayList<>();

        for (Item item : getAll()) {
            if (item.getTags().containsAll(tags)) {
                items.add(item);
            }
        }
        return items;
    }
}