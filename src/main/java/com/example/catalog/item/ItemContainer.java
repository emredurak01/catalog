package com.example.catalog.item;

import com.example.catalog.exception.item.ItemNotExistException;
import com.example.catalog.exception.type.TypeNotExistException;
import com.example.catalog.type.TypeContainer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

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

    public void write() throws IOException {
        FileWriter w = new FileWriter(getClass().getSimpleName() + ".ser");
        Formatter f = new Formatter(w);

        for (Item item : items) {
            StringBuilder s = new StringBuilder(item.getName() + ", " + item.getType().getName() + ", ");

            for (int i = 0; i < item.getFieldValues().size(); i++) {
                s.append(item.getFieldValues().get(i)).append(", ");
            }

            for (String tag : item.getTags()) {
                s.append("#").append(tag).append(", ");
            }
            s = new StringBuilder(s.substring(0, s.length() - 2));
            f.format("%s%n", s);
        }
        f.close();
        w.close();
    }

    public void read(TypeContainer typeContainer) {
        try {
            Scanner s = new Scanner(Paths.get(getClass().getSimpleName() + ".ser"));

            while (s.hasNextLine()) {
                String[] line = s.nextLine().split(",");

                for (int i = 0; i < line.length; i++) {
                    line[i] = line[i].trim();
                }
                ArrayList<String> fieldValues = new ArrayList<>();
                Set<String> tags = new HashSet<>();

                for (int i = 2; i < line.length; i++) {
                    if (line[i].startsWith("#")) {
                        tags.add(line[i].substring(1));
                    } else {
                        fieldValues.add(line[i]);
                    }
                }
                Item item = new Item(line[0], typeContainer.get(line[1]), fieldValues);
                item.setTags(tags);
                items.add(item);
                item.getType().getChildren().add(item);
            }
            s.close();
        } catch (IOException | TypeNotExistException e) {
            e.printStackTrace();
        }
    }
}
