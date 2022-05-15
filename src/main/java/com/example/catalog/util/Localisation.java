package com.example.catalog.util;

public class Localisation {
    public static final String HELP_TITLE = "Help";
    public static final String HELP_ADD_TEXT = """
    1. Select the root in the tree view and click the add button to add a type. Use comma (,) to insert multiple field types.
    2. Select a type and click the add button to add an item.
    3. Select an item and click the add tag button after typing the tag name under the button.
    """;
    public static final String HELP_REMOVE_EDIT_TEXT = """
    1. Select a type or an item in the tree view and click the remove or edit button.
    2. While editing an item, you can enter multiple tags by inserting comma (,).
    """;
    public static final String HELP_SEARCH_TEXT = """
    1. Use the checkboxes at right to filter items by tags.
    2. Use search bar at top to filter items and types by name.
    3. All these operations can filter the items and types simultaneously.
    WARNING: If you filter by type name, the items of that type are not shown, since you are not interested in the items.
    """;
    public static final String HELP_SAVE_TEXT = """
    1. Click the save button. It should create the necessary data files with the .save extension.
    """;
    public static final String HELP_EXPORT_TEXT = """
    1. Click the export button to export the tree view as .html file.
    """;
    public static final String HELP_PRINT_TEXT = """
    1. Click the print button and select the printer to print the tree view as .pdf file.
    """;
    public static final String SELECT = "Select";
    public static final String ADD = "Add";
    public static final String ROOT = "Root";
    public static final String TYPE = "Type";
    public static final String ITEM = "Item";
    public static final String EMPTY_NAME = "Name is empty";
    public static final String BLANK_FIELD = "Field is blank";
    public static final String REMOVE_BUTTON = "-";
    public static final String ADD_BUTTON = "+";
    public static final String SELECTED_ROOT = "Root is selected";
    public static final String SELECTED_TYPE = "Type is selected";
    public static final String BLANK_TAG = "Tag is blank";
    public static final String USED_COMMA = "Comma is not allowed";
    public static final String EMPTY_TAG = "Tag can not be blank";
    public static final String SAVE_SUCCESS = "Successful save";
    public static final String SHORTCUT = """
            Ctrl + S = Save
            Ctrl + E = Export
            Ctrl + P = Print
            Ctrl + C = Close
            Ctrl + H = Help
    """;
}
