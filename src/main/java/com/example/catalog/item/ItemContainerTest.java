package com.example.catalog.item;

import com.example.catalog.exception.item.ItemNotExistException;
import com.example.catalog.exception.tag.TagExistException;
import com.example.catalog.type.Type;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemContainerTest {
    @Test
    public void getAll() {
        ItemContainer itemContainer = new ItemContainer();
        List<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("title");
        fieldTypes.add("author");
        fieldTypes.add("date");
        List<String> fieldValues = new ArrayList<>();
        fieldTypes.add("star wars");
        fieldTypes.add("obi wan");
        fieldTypes.add("a long time ago");
        itemContainer.add(new Item("book", new Type("book", fieldTypes), fieldValues));
        assert itemContainer.getAll().size() == 1;
    }

    @Test
    public void add() {
        ItemContainer itemContainer = new ItemContainer();
        List<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("title");
        fieldTypes.add("author");
        fieldTypes.add("date");
        List<String> fieldValues = new ArrayList<>();
        fieldValues.add("star wars");
        fieldValues.add("obi wan");
        fieldValues.add("a long time ago");
        String itemName = "hello there";
        String typeName = "book";
        Item item = new Item(itemName, new Type(typeName, fieldTypes), fieldValues);
        itemContainer.add(item);
        assert itemContainer.getAll().size() == 1 && itemContainer.getAll().get(0).getName().equals(itemName) &&
                itemContainer.getAll().get(0).getFieldValues().get(0).equals(fieldValues.get(0)) &&
                itemContainer.getAll().get(0).getFieldValues().get(1).equals(fieldValues.get(1)) &&
                itemContainer.getAll().get(0).getFieldValues().get(2).equals(fieldValues.get(2));
    }

    @Test
    public void remove() throws ItemNotExistException {
        ItemContainer itemContainer = new ItemContainer();
        List<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("title");
        fieldTypes.add("author");
        fieldTypes.add("date");
        List<String> fieldValues = new ArrayList<>();
        fieldValues.add("star wars");
        fieldValues.add("obi wan");
        fieldValues.add("a long time ago");
        String itemName = "hello there";
        String typeName = "book";
        Item item = new Item(itemName, new Type(typeName, fieldTypes), fieldValues);
        itemContainer.add(item);
        itemContainer.remove(item);
        assert itemContainer.getAll().size() == 0;
    }

    @Test
    public void getByTags() throws TagExistException {
        ItemContainer itemContainer = new ItemContainer();
        List<String> fieldTypes = new ArrayList<>();
        fieldTypes.add("title");
        fieldTypes.add("author");
        fieldTypes.add("date");
        List<String> fieldValues = new ArrayList<>();
        fieldValues.add("star wars");
        fieldValues.add("obi wan");
        fieldValues.add("a long time ago");
        String itemName = "hello there";
        String typeName = "book";
        Item item = new Item(itemName, new Type(typeName, fieldTypes), fieldValues);
        item.addTag("favourite");
        item.addTag("borrowed");
        itemContainer.add(item);
        assert item.getTags().size() == 2 && item.getTags().contains("favourite") &&
                item.getTags().contains("borrowed") && !item.getTags().contains("some random value");
    }
}