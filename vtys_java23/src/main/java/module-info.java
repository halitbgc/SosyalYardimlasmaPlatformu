module com.example.vtys_java23 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.vtys_java23 to javafx.fxml;
    exports com.example.vtys_java23;
}