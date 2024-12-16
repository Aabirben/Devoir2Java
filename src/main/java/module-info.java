module org.example.dev2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;


    opens org.example.dev2 to javafx.fxml;
    exports org.example.dev2;
}