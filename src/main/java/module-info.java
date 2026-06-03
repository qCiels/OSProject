module OSProject {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.OperatingSystems to javafx.fxml;
    exports org.OperatingSystems;
}