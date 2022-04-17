module com.example.catalog {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.example.catalog to javafx.fxml;
    exports com.example.catalog;
    exports com.example.catalog.exception;
    opens com.example.catalog.exception to javafx.fxml;
    exports com.example.catalog.exception.field;
    opens com.example.catalog.exception.field to javafx.fxml;
    exports com.example.catalog.exception.type;
    opens com.example.catalog.exception.type to javafx.fxml;
}
