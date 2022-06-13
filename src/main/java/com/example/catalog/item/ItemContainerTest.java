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
        fieldValues.add(" "); // blank field, it should be allowed for item values; however, not for type field types
        fieldValues.add(""); // empty field, it should be allowed for item field values; however, not for type field types
        String itemName = "hello there";
        String typeName = "book";

        // add 10 different items, which 2 of them have the same type; if the length is 10, then the test works
        for (int i = 0; i < 10; i++) {
            itemContainer.add(new Item(itemName + i, new Type(typeName + (i % 2), fieldTypes), fieldValues));
        }
        //add item with blank name
        Item blankItem = new Item(" ", new Type(typeName, fieldTypes), fieldValues);
        //add item with empty name
        Item emptyItem = new Item("", new Type(typeName, fieldTypes), fieldValues);
        System.out.println(itemContainer.getAll().size());
        // test the fields of only one of them, since the length is tested
        assert itemContainer.getAll().size() == 12 - 2;
        assert itemContainer.getAll().get(0).getName().equals(itemName + 0);
        assert itemContainer.getAll().get(0).getFieldValues().get(0).equals(fieldValues.get(0));
        assert itemContainer.getAll().get(0).getFieldValues().get(1).equals(fieldValues.get(1));
        assert itemContainer.getAll().get(0).getFieldValues().get(2).equals(fieldValues.get(2));
        assert itemContainer.getAll().get(0).getFieldValues().get(3).equals(fieldValues.get(3));
        // the item container would not contain the below items, if the add operation were not successful
        assert !itemContainer.getAll().contains(blankItem);
        assert !itemContainer.getAll().contains(emptyItem);
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
        item.addTag(" "); // the blank tags are not allowed, and they will not be added
        item.addTag(""); // the empty tags are not allowed, and they will not be added
        itemContainer.add(item);
        // therefore, the final size of the tags should be 2
        assert item.getTags().size() == 2 && item.getTags().contains("favourite") &&
                item.getTags().contains("borrowed") && !item.getTags().contains("some random value");
    }
}