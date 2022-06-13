package com.example.catalog.type;


import com.example.catalog.exception.type.TypeExistException;
import com.example.catalog.exception.type.TypeNotExistException;
import com.example.catalog.item.ItemContainer;
import javafx.scene.control.TreeView;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class TypeContainer {

    private final List<Type> types;

    public TypeContainer() {
        types = new ArrayList<>();
    }

    public void add(Type type, TreeView<String> view) throws TypeExistException {
        try {
            get(type.getName());
            throw new TypeExistException();
        } catch (TypeNotExistException e) {
            if (!type.getName().isBlank()) {
                boolean[] flag = {true};
                type.getFieldTypes().forEach(s -> {
                    if (s.isBlank()) {
                        flag[0] = false;
                    }
                });

                if (flag[0]) {
                    types.add(type);
                }
                view.getRoot().getChildren().add(type);
            }
        }
    }

    public void remove(Type type, TreeView<String> view, ItemContainer itemContainer) throws TypeNotExistException {
        if (!types.contains(type)) {
            throw new TypeNotExistException();
        }
        types.remove(type);
        view.getRoot().getChildren().remove(type);
        itemContainer.getAll().removeIf(t -> t.getType().getName().equals(type.getName()));
    }

    public Type get(String name) throws TypeNotExistException {
        for (Type type : types) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        throw new TypeNotExistException();
    }

    public List<Type> getAll() {
        return types;
    }

    public void write() throws IOException {
        FileWriter w = new FileWriter("types.save");
        Formatter f = new Formatter(w);

        for (Type t : types) {
            f.format("%s, ", t.getName());

            for (int i = 0; i < t.getFieldTypes().size(); i++) {
                f.format("%s", t.getFieldTypes().get(i));

                if (i != t.getFieldTypes().size() - 1) {
                    f.format(", ");
                }
            }
            f.format("%n");
        }
        f.close();
        w.close();
    }

    public void read(TreeView<String> view) {
        try {
            Scanner s = new Scanner(Paths.get("types.save"));
            view.getRoot().getChildren().clear();

            while (s.hasNextLine()) {
                String[] line = s.nextLine().split(",");

                for (int i = 0; i < line.length; i++) {
                    line[i] = line[i].trim();
                }

                ArrayList<String> fieldTypes = new ArrayList<>(Arrays.asList(line).subList(1, line.length));
                Type type = new Type(line[0], fieldTypes);
                types.add(type);
                view.getRoot().getChildren().add(type);
            }
            s.close();
        } catch (IOException e) {
            System.out.println(e.getMessage() + " could not be found.");
        }
    }
}